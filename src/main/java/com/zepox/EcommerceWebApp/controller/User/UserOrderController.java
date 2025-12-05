package com.zepox.EcommerceWebApp.controller.User;

import com.stripe.exception.StripeException;
import com.zepox.EcommerceWebApp.dto.request.CheckoutOrderRequestDto;
import com.zepox.EcommerceWebApp.dto.request.UpdateOrderStatusRequestDto;
import com.zepox.EcommerceWebApp.dto.response.GetOrderByUserIdResponseDto;
import com.zepox.EcommerceWebApp.dto.response.GetOrdersByUserIdResponseDto;
import com.zepox.EcommerceWebApp.dto.response.StripeResponse;
import com.zepox.EcommerceWebApp.service.OrderService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/order")
public class UserOrderController {
    private final OrderService orderService;

    @GetMapping("/getOrderByUserId")
    public ResponseEntity<GetOrderByUserIdResponseDto> getOrderByUserId(
            @RequestParam @NotBlank(message = "Order Id is missing") String orderId
    ){
        return ResponseEntity.ok().body(orderService.getOrderByUserId(orderId));
    }

    @GetMapping("/getOrdersByUserId")
    public ResponseEntity<GetOrdersByUserIdResponseDto> getOrdersByUserId(){
        return ResponseEntity.ok().body(orderService.getOrdersByUserId());
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkout(@RequestBody CheckoutOrderRequestDto dto) throws StripeException {
        return ResponseEntity.ok().body(orderService.checkout(dto));
    }

    @PostMapping("/updateOrderStatus")
    public ResponseEntity<String> updateOrderStatus(@RequestBody UpdateOrderStatusRequestDto dto){
        orderService.updateOrderStatus(dto);
        return ResponseEntity.ok().body("Order Status Updated");
    }

}
