package org.gardenfebackend.security;

import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.access-exp-hours}")
    private long accessExpHours;

    @Value("${security.jwt.refresh-exp-hours}")
    private long refreshExpHours;

    @Value("${spring.security.google.client-id}")
    private String googleClientId;

    @Value("${telegram.bot.token}")
    private String botToken;


    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(userDetails, accessExpHours, Map.of());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, refreshExpHours, Map.of("type", "refresh"));
    }

    public Instant getRefreshExpirationInstant() {
        return Instant.now().plus(refreshExpHours, ChronoUnit.HOURS);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String buildToken(UserDetails userDetails, long expHours, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        Instant expiration = now.plus(expHours, ChronoUnit.HOURS);

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Не удалось инициализировать JWT ключ", e);
        }
    }

    public GoogleIdToken.Payload verifyGoogleToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new IllegalArgumentException("Invalid ID token");
            }
            return idToken.getPayload();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to verify Google ID token", e);
        }
    }

    public boolean verifyTelegramAuth(Map<String, String> data) {
        try {
            String receivedHash = data.get("hash");
            if (receivedHash == null)
                return false;

            Map<String, String> sorted = new TreeMap<>(data);
            sorted.remove("hash");

            StringBuilder dataCheck = new StringBuilder();
            for (Map.Entry<String, String> entry : sorted.entrySet()) {
                dataCheck.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("\n");
            }
            if (dataCheck.length() > 0)
                dataCheck.deleteCharAt(dataCheck.length() - 1);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] secretKey = digest.digest(botToken.getBytes(StandardCharsets.UTF_8));

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey, "HmacSHA256"));

            byte[] hash = mac.doFinal(dataCheck.toString().getBytes(StandardCharsets.UTF_8));
            String computed = bytesToHex(hash);

            return computed.equalsIgnoreCase(receivedHash);
        } catch (Exception e) {
            return false;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}


