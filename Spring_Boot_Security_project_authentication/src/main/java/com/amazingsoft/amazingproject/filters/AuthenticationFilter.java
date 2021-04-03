package com.amazingsoft.amazingproject.filters;

import com.amazingsoft.amazingproject.managers.ASAuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private ASAuthenticationManager authenticationManager;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        logger.info("JwtRequestFilter running...");

        if (null == authorizationHeader) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        logger.info("Authentication header is present...");

        boolean headerHasCorrectForm = authorizationHeader.matches("Bearer .*");

        if (headerHasCorrectForm) {
            logger.info("Authentication header is formatted correctly: " + authorizationHeader);

            String bearerToken = authorizationHeader.split(" ")[1];
            authenticationManager.authenticate(new BearerTokenAuthenticationToken(bearerToken));
            logger.info("User authenticated");
        } else {
            logger.info("Bad Authorization header");
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
