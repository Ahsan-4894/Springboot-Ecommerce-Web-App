package com.zepox.EcommerceWebApp.exception.custom;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }
}
