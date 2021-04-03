package com.amazingsoft.amazingproject.services;

import com.amazingsoft.amazingproject.entity.Booking;
import com.amazingsoft.amazingproject.entity.User;
import com.amazingsoft.amazingproject.repositories.BookingRepository;
import com.amazingsoft.amazingproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingRetrievalService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Booking> findAllForCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof DefaultOAuth2AuthenticatedPrincipal) {
            username = ((DefaultOAuth2AuthenticatedPrincipal) principal).getName();
        } else {
            throw new RuntimeException("Some exception that sould be replaced "); // TODO
        }

        User user = userRepository.findByUsername(username);

        return bookingRepository.findAllByUser(user);
    }
}
