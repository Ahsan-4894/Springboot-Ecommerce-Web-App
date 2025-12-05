package com.zepox.EcommerceWebApp.dto.request;


public record SearchProductsRequestDto(String id, String name, String available, String category, double priceLowerBound, double priceUpperBound) {
}
