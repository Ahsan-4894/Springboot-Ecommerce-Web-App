package com.zepox.EcommerceWebApp.dto.request;

public record GiveFeedbackDto(String email, String feedback, String name, String rating, String productId) {
}
