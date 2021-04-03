package com.amazingsoft.amazingproject.exceptions;

import org.springframework.web.client.RestClientException;

public class OverlappingBookingException extends RestClientException {
    public OverlappingBookingException() {
        super("Booking request overlapps with other existing bookings.");
    }
}
