package com.zepox.EcommerceWebApp.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categ_id")
    private int id;

    @Column(name = "categ_name")
    private String categoryName;
    @Column(name = "categ_description")
    private String categoryDescription;

    @ManyToOne
    @JoinColumn(name = "adminid")
    private Admin admin;

    @OneToMany(mappedBy = "category")
    private List<Item> item;
}
