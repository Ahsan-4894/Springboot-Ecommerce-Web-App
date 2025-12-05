package com.zepox.EcommerceWebApp.exception.custom;

public class PaymentDoesNotExistException extends RuntimeException {
    public PaymentDoesNotExistException(String message) {
        super(message);
    }
}
