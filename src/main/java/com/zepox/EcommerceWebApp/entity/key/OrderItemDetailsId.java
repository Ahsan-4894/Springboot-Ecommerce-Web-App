package com.zepox.EcommerceWebApp.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDetailsId {
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "item_id")
    private String itemId;

}
