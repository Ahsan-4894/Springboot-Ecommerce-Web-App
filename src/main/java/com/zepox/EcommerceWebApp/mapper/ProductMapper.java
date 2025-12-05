package com.zepox.EcommerceWebApp.mapper;

import com.zepox.EcommerceWebApp.dto.response.GetProductsResponseDto;
import com.zepox.EcommerceWebApp.dto.response.ProductsResponseDto;
import com.zepox.EcommerceWebApp.entity.Category;
import com.zepox.EcommerceWebApp.entity.Item;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {
    public ProductsResponseDto toDto(Item item){
        return ProductsResponseDto.builder()
                .id(item.getId())
                .categoryname(item.getCategory().getCategoryName())
                .name(item.getName())
                .price(item.getPrice())
                .imgUrl(item.getImgUrl())
                .quantity(item.getQuantity() - item.getReservedStock())
                .stocks(item.getQuantity() > 0 ? "stock" : "outofstock")
                .description(item.getDescription())
                .url("/product/"+item.getId())
                .rating("5")
                .build();
    }
    public List<ProductsResponseDto> toDtos(List<Item> items){
        return items
                .stream()
                .map(this::toDto)
                .toList();
    }

   
}
