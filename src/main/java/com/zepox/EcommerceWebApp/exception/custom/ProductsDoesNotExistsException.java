package com.zepox.EcommerceWebApp.exception.custom;

public class ProductsDoesNotExistsException extends RuntimeException {
    public ProductsDoesNotExistsException(String message) {
        super(message);
    }
}
