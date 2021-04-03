package com.amazingsoft.amazingproject.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class BearerTokenGeneratorService {

    private final SecureRandom secureRandom = new SecureRandom();

    public String generateToken() {
        byte[] userTokenBytes = new byte[128];
        secureRandom.nextBytes(userTokenBytes);

        return Base64.getEncoder().encodeToString(userTokenBytes);
    }
}
