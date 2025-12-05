package com.zepox.EcommerceWebApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
public class Category extends BaseEntity{
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
    @JsonIgnore
    @ToString.Exclude
    private User admin;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    @ToString.Exclude
    private List<Item> item;
}
