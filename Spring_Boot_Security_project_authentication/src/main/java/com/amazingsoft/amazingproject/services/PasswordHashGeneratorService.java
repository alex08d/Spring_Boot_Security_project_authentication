package com.amazingsoft.amazingproject.services;

import com.amazingsoft.amazingproject.entity.User;
import com.amazingsoft.amazingproject.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class PasswordHashGeneratorService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MessageDigest messageDigest;
    {
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Message digest error", e);
            throw new RuntimeException(e);
        }
    }

    @Autowired
    private UserRepository userRepository;

    public String generateHashFromUserAndPassword(String username, String password) {
        User user = userRepository.findByUsername(username);
        String salt = user.getSalt();

        return generateHashFromPasswordAndSalt(password, salt);
    }

    public String generateHashFromPasswordAndSalt(String password, String salt) {
        String passwordWithSalt = password + salt;
        byte[] digest = messageDigest.digest(passwordWithSalt.getBytes());

        return Base64.getEncoder().encodeToString(digest);
    }
}
