package com.zepox.EcommerceWebApp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zepox.EcommerceWebApp.entity.type.OrderStatusType;
import com.zepox.EcommerceWebApp.entity.type.PaymentStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutOrderRequestDto {
    private Cart cart;
    private ShippingDetails shippingDetails;
    private OrderDetails orderDetails;
    private User user;
    private PaymentDetails paymentDetails;
    private String currency;

    @Data
    public static class Cart{
        public List<CartItem> cartItems;
    }
    @Data
    public static class CartItem{
        private String details;
        private double price;
        private int quantity;
    }

    @Data
    public static class ShippingDetails{
        private String name;
        private String address;
        private String city;
        private String postalCode;
        private String contactNumber;
    }

    @Data
    public static class OrderDetails{
        private double price;
        @JsonProperty("orderplaced")
        private LocalDateTime orderPlaced;
    }

    @Data
    public static class User{
        private String id;
        private String name;
    }

    @Data
    public static class PaymentDetails{
        private String paymentMethod;
    }
}
