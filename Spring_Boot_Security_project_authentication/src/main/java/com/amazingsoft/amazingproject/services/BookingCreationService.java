package com.amazingsoft.amazingproject.services;

import com.amazingsoft.amazingproject.entity.Booking;
import com.amazingsoft.amazingproject.entity.CreateBookingRequest;
import com.amazingsoft.amazingproject.exceptions.OverlappingBookingException;
import com.amazingsoft.amazingproject.repositories.BookingRepository;
import com.amazingsoft.amazingproject.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class BookingCreationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String username;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    public Booking create(CreateBookingRequest createBookingRequest) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof DefaultOAuth2AuthenticatedPrincipal) {
             username = ((DefaultOAuth2AuthenticatedPrincipal)principal).getName();
        } else {
             username = principal.toString();
        }
        logger.info(username);


        Booking booking = new Booking()
                .setFromTimestamp(createBookingRequest.getFromTimestamp())
                .setToTimestamp(createBookingRequest.getToTimestamp())
                .setUser(userRepository.findByUsername(username));

        return bookingRepository.save(booking);
    }
}
