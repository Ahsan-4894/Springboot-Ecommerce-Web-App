package com.zepox.EcommerceWebApp.entity;

import com.zepox.EcommerceWebApp.entity.type.StatusType;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name="shippingid")
    private ShippingDetails shippingDetails;

    private double price;
    @Enumerated(EnumType.STRING)
    private StatusType status;
    @Column(name = "orderplaced")
    private Date orderPlaced;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;
}
