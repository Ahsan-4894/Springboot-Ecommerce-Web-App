package com.zepox.EcommerceWebApp.mapper;

import com.zepox.EcommerceWebApp.dto.response.OrdersResponseDto;
import com.zepox.EcommerceWebApp.entity.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OrderMapper {
    public OrdersResponseDto toDto(Order order, int id){
        return OrdersResponseDto.builder()
                .id(id)
                .userId(order.getUser().getId())
                .date(LocalDateTime.now())
                .status(order.getStatus())
                .total(order.getPrice())
                .shippingId(order.getShippingDetails().getId())
                .build();
    }
    public List<OrdersResponseDto> toDtos(List<Order> orders){
        AtomicInteger counter = new AtomicInteger(0);
        return orders
                .stream()
                .map(order-> toDto(order, counter.incrementAndGet()))
                .toList();
    }
}
