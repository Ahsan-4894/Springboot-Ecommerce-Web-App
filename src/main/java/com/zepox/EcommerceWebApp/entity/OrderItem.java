package com.zepox.EcommerceWebApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zepox.EcommerceWebApp.entity.key.OrderItemDetailsId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orderitems")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem extends BaseEntity{
    @EmbeddedId
    private OrderItemDetailsId id;

    private int quantity;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    @JsonIgnore
    @ToString.Exclude
    private Order order;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    @JsonIgnore
    @ToString.Exclude
    private Item item;

}
