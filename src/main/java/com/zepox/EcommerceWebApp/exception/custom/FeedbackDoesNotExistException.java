package com.zepox.EcommerceWebApp.exception.custom;

public class FeedbackDoesNotExistException extends RuntimeException {
    public FeedbackDoesNotExistException(String message) {
        super(message);
    }
}
