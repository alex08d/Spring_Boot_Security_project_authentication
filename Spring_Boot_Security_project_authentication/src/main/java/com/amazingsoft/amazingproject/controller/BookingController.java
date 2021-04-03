package com.amazingsoft.amazingproject.controller;

import com.amazingsoft.amazingproject.entity.Booking;
import com.amazingsoft.amazingproject.entity.CreateBookingRequest;
import com.amazingsoft.amazingproject.repositories.BookingRepository;
import com.amazingsoft.amazingproject.services.BookingCreationService;
import com.amazingsoft.amazingproject.services.BookingRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingCreationService bookingCreationService;

    @Autowired
    private BookingRetrievalService bookingRetrievalService;

    @GetMapping("/default")
    public ResponseEntity<String> getDefaultBooking() {

        ResponseEntity<String> responseEntity = new ResponseEntity<>("ok", HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody CreateBookingRequest createBookingRequest) {
        Booking booking = bookingCreationService.create(createBookingRequest);
        return ResponseEntity.ok(booking);
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAll() {
        List<Booking> bookingsForCurrentUser = bookingRetrievalService.findAllForCurrentUser();
        return ResponseEntity.ok(bookingsForCurrentUser);
    }
}
