package com.amazingsoft.amazingproject.managers;

import com.amazingsoft.amazingproject.entity.User;
import com.amazingsoft.amazingproject.entity.UserToken;
import com.amazingsoft.amazingproject.repositories.UserRepository;
import com.amazingsoft.amazingproject.repositories.UserTokenRepository;
import com.amazingsoft.amazingproject.services.BearerTokenGeneratorService;
import com.amazingsoft.amazingproject.services.PasswordHashGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CustomAuthenticationManager implements ASAuthenticationManager {

    private static final HashMap<String, Object> defaultAttributes = new HashMap<>();
    static {
        defaultAttributes.put("user_type", "anonymousUser");
    }

    private static final List<GrantedAuthority> defaultAuthorities = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BearerTokenGeneratorService bearerTokenGeneratorService;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHashGeneratorService passwordHashGeneratorService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.isAuthenticated()) {
            return authentication;
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            return authenticate((UsernamePasswordAuthenticationToken) authentication);
        } else if (authentication instanceof BearerTokenAuthenticationToken) {
            return authenticate((BearerTokenAuthenticationToken) authentication);
        }

        throw new BadCredentialsException("Unsupported authentication");
    }

    @Override
    public BearerTokenAuthentication authenticate(UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        String passwordHashGenerated = passwordHashGeneratorService.generateHashFromUserAndPassword(username, password);
        String passwordHashSaved = userRepository.findByUsername(username).getPassword();
        if (!passwordHashGenerated.equals(passwordHashSaved)) {
            throw new BadCredentialsException("Bad credentials");
        }

        String token = bearerTokenGeneratorService.generateToken();

        DefaultOAuth2AuthenticatedPrincipal principal = new DefaultOAuth2AuthenticatedPrincipal(username, defaultAttributes, defaultAuthorities);
        Instant emittedAt = Instant.now();
        Instant expiresAt = Instant.now().plus(Duration.ofHours(24));
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, token, emittedAt, expiresAt);
        BearerTokenAuthentication bearerTokenAuthentication = new BearerTokenAuthentication(principal, oAuth2AccessToken, defaultAuthorities);

        logger.info("Created token for user {} at {}, expires at {}, token value: {}", username, emittedAt, expiresAt, token);

        bearerTokenAuthentication.setAuthenticated(true);

        userTokenRepository.save(
                new UserToken()
                        .setUser(userRepository.findByUsername(username))
                        .setToken(token)
                        .setEmittedAtTimestamp(emittedAt.getEpochSecond())
                        .setExpiresAtTimestamp(expiresAt.getEpochSecond())
        );

        return bearerTokenAuthentication;
    }

    @Override
    public BearerTokenAuthentication authenticate(BearerTokenAuthenticationToken authentication) throws AuthenticationException {
        String token = authentication.getToken();
        UserToken userToken = userTokenRepository.findByToken(token);
        User user = userToken.getUser();

        DefaultOAuth2AuthenticatedPrincipal principal = new DefaultOAuth2AuthenticatedPrincipal(user.getUsername(), defaultAttributes, defaultAuthorities);
        Instant issuedAt = Instant.ofEpochSecond(userToken.getEmittedAtTimestamp());
        Instant expiresAt = Instant.ofEpochSecond(userToken.getExpiresAtTimestamp());
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, token, issuedAt, expiresAt);
        BearerTokenAuthentication bearerTokenAuthentication = new BearerTokenAuthentication(principal, oAuth2AccessToken, defaultAuthorities);

        boolean accessTokenNotExpired = expiresAt.isAfter(Instant.now());
        bearerTokenAuthentication.setAuthenticated(accessTokenNotExpired);
        SecurityContextHolder.getContext().setAuthentication(bearerTokenAuthentication);

        return bearerTokenAuthentication;
    }
}
