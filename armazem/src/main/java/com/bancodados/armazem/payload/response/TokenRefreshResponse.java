package com.bancodados.armazem.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshResponse {
    private String access;
    private String refresh;

    public TokenRefreshResponse(String accessToken, String refreshToken) {
        this.access = accessToken;
        this.refresh = refreshToken;
    }
}