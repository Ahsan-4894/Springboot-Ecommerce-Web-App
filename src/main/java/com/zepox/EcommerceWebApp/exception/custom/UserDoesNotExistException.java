package com.zepox.EcommerceWebApp.exception.custom;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(String message) {
        super(message);
    }
}
