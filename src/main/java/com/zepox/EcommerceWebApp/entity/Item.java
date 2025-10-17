package com.zepox.EcommerceWebApp.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private double price;
    private int quantity;

    @Column(name = "public_id")
    private String publicId;
    @Column(name = "img_url")
    private String imgUrl;

    private String description;

    @ManyToOne
    @JoinColumn(name = "adminid")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "categoryid")
    private Category category;

    @OneToMany(mappedBy = "item")
    private List<Feedback> feedback;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems;
}
