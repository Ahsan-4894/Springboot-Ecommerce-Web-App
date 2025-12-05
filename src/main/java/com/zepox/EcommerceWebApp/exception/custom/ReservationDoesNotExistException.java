package com.zepox.EcommerceWebApp.exception.custom;

public class ReservationDoesNotExistException extends RuntimeException {
    public ReservationDoesNotExistException(String message) {
        super(message);
    }
}
