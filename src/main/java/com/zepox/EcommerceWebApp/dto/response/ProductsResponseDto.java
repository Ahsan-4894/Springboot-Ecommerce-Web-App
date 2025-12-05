package com.zepox.EcommerceWebApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductsResponseDto implements Serializable {
    private String id;
    private String categoryname;
    private String name;
    private double price;
    private String imgUrl;
    private String stocks;
    private int quantity;
    private String description;
    private String url;
    private String rating;
}