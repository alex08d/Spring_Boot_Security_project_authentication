package com.amazingsoft.amazingproject.managers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;

public interface ASAuthenticationManager extends AuthenticationManager {
    Authentication authenticate(UsernamePasswordAuthenticationToken userPasswordToken);
    Authentication authenticate(BearerTokenAuthenticationToken bearerToken);
}
