package com.ludoelite.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Payload received from POST /api/auth/login on success.
 * Contains the JWT token and basic user info.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private User   user;

    public AuthResponse() {}

    public String getToken()               { return token; }
    public void   setToken(String token)   { this.token = token; }

    public String getTokenType()                   { return tokenType; }
    public void   setTokenType(String tokenType)   { this.tokenType = tokenType; }

    public User getUser()              { return user; }
    public void setUser(User user)     { this.user = user; }
}
