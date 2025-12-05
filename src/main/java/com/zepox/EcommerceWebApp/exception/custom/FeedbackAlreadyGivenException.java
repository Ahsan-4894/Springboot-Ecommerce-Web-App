package com.zepox.EcommerceWebApp.exception.custom;

public class FeedbackAlreadyGivenException extends RuntimeException {
    public FeedbackAlreadyGivenException(String message) {
        super(message);
    }
}
