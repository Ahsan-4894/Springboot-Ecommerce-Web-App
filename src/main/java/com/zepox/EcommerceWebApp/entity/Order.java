package com.zepox.EcommerceWebApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zepox.EcommerceWebApp.entity.type.OrderStatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties({"orders", "items", "password", "hibernateLazyInitializer"})
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name="shippingid")
    @JsonIgnoreProperties({"orders", "hibernateLazyInitializer"})
    @ToString.Exclude
    private ShippingDetails shippingDetails;

    private double price;

    @Enumerated(EnumType.STRING)
    private OrderStatusType status;

    @Column(name = "orderplaced")
    private LocalDateTime orderPlaced;


    @OneToOne(mappedBy = "order")
    @JsonIgnore
    @ToString.Exclude
    private Payment payment;

    @OneToMany(mappedBy = "order")
    @JsonIgnore
    @ToString.Exclude
    private List<OrderItem> orderItems;

    @OneToOne(mappedBy = "order")
    @JsonIgnore
    @ToString.Exclude
    private Reservation reservation;

    @Column(name = "stripe_session_id")
    private String stripeSessionId;
}
