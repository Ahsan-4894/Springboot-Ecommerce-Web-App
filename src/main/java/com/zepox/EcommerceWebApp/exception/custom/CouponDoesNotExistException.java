package com.zepox.EcommerceWebApp.exception.custom;

public class CouponDoesNotExistException extends RuntimeException {
    public CouponDoesNotExistException(String message) {
        super(message);
    }
}
