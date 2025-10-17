package com.zepox.EcommerceWebApp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "adminid")
    private Admin admin;

    @Column(name = "discount_rate")
    private double discountRate;

    @Column(name = "is_used")
    private int isUsed;
}
