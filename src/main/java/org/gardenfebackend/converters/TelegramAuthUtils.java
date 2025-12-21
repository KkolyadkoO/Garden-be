package org.gardenfebackend.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class TelegramAuthUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Map<String, String> decodeBase64JsonToMap(String base64) {
        try {
            byte[] decoded = Base64.getDecoder().decode(base64);
            String json = new String(decoded, StandardCharsets.UTF_8);
            return MAPPER.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to decode Telegram data", e);
        }
    }
}
