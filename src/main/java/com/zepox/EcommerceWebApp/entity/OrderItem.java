package com.zepox.EcommerceWebApp.entity;

import com.zepox.EcommerceWebApp.entity.key.OrderItemDetailsId;
import jakarta.persistence.*;

@Entity
@Table(name = "orderitems")
public class OrderItem {
    @EmbeddedId
    private OrderItemDetailsId id;

    private int quantity;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "orderId")
    private Order order;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "itemId")
    private Item item;

}
