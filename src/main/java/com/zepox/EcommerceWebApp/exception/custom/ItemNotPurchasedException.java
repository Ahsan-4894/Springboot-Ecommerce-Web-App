package com.zepox.EcommerceWebApp.exception.custom;

public class ItemNotPurchasedException extends RuntimeException {
    public ItemNotPurchasedException(String message) {
        super(message);
    }
}
