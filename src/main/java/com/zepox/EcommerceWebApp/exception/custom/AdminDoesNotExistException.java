package com.zepox.EcommerceWebApp.exception.custom;

public class AdminDoesNotExistException extends RuntimeException {
    public AdminDoesNotExistException(String message) {
        super(message);
    }
}
