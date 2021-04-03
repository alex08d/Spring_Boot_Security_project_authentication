package com.amazingsoft.amazingproject.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {

    @JsonProperty
    private String jwtToken;

    public String getJwtToken() {
        return jwtToken;
    }

    public AuthenticationResponse setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
        return this;
    }
}
