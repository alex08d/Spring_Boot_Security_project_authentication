package com.amazingsoft.amazingproject.controller;

import com.amazingsoft.amazingproject.entity.AuthenticationRequest;
import com.amazingsoft.amazingproject.entity.AuthenticationResponse;
import com.amazingsoft.amazingproject.entity.User;
import com.amazingsoft.amazingproject.managers.ASAuthenticationManager;
import com.amazingsoft.amazingproject.repositories.UserRepository;
import com.amazingsoft.amazingproject.services.BearerTokenGeneratorService;
import com.amazingsoft.amazingproject.services.PasswordHashGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private ASAuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BearerTokenGeneratorService tokenGeneratorService;

    @Autowired
    private PasswordHashGeneratorService passwordHashGeneratorService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        if (authenticate instanceof BearerTokenAuthentication) {
            return ResponseEntity.ok(
                    new AuthenticationResponse().setJwtToken(((BearerTokenAuthentication) authenticate).getToken().getTokenValue())
            );
        }

        throw new BadCredentialsException("Bad credentials");
    }

    @PostMapping("/sign-up")
    public ResponseEntity<User> signup(@RequestBody AuthenticationRequest authenticationRequest) {

        String salt = tokenGeneratorService.generateToken();

        String userPassword = passwordHashGeneratorService.generateHashFromPasswordAndSalt(authenticationRequest.getPassword(), salt);

        User user = userRepository.save(
                new User()
                        .setUsername(authenticationRequest.getUsername())
                        .setPassword(userPassword)
                        .setSalt(salt)
        );

        return ResponseEntity.ok(user);
    }
}
