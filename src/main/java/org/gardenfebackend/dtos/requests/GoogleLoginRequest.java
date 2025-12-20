package org.gardenfebackend.dtos.requests;

import lombok.Data;

@Data
public class GoogleLoginRequest {
    private String idToken;
}
