package com.zepox.EcommerceWebApp.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String role;

    @OneToMany(mappedBy = "admin")
    private List<Coupon> coupons;

    @OneToMany(mappedBy = "admin")
    private List<Category> categories;

    @OneToMany(mappedBy = "admin")
    private List<Item> items;
}
