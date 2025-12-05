package com.zepox.EcommerceWebApp.controller.Admin;

import com.zepox.EcommerceWebApp.dto.response.OrdersResponseDto;
import com.zepox.EcommerceWebApp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {
    private final OrderService orderService;

    @GetMapping("/getOrders")
    public ResponseEntity<OrdersResponseDto> getOrders(){
        return ResponseEntity.ok().body(orderService.getOrders());
    }

    @GetMapping("/getOrderDetails")
    public ResponseEntity<OrdersResponseDto> getOrderDetails(
            @RequestParam String orderId,
            @RequestParam String userId
    ) {
        return ResponseEntity.ok().body(orderService.getOrderDetails(
                orderId,
                userId
        ));
    }

    @PostMapping("/searchOrders")
    public ResponseEntity<OrdersResponseDto> searchOrders(){
        return ResponseEntity.ok().body(orderService.searchOrders());
    }
}
