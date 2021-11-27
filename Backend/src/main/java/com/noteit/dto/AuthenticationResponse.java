package com.noteit.dto;

public class AuthenticationResponse {

    private final Long userId;
    private final String jwtToken;

    public AuthenticationResponse(Long userId, String jwtToken) {
        this.userId = userId;
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public Long getUserId() {
        return userId;
    }
}
