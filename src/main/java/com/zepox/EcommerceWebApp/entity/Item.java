package com.zepox.EcommerceWebApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Auditable;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "items")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", columnDefinition = "varchar(255)")
    private String name;

    private double price;
    private int quantity;

    @Column(name = "reserved_stock")
    private int reservedStock;

    @Column(name = "public_id")
    private String publicId;
    @Column(name = "img_url")
    private String imgUrl;

    private String description;

    @ManyToOne
    @JoinColumn(name = "adminid")
    @JsonIgnoreProperties({"items", "password", "orders", "hibernateLazyInitializer"})
    private User admin;

    @ManyToOne
    @JoinColumn(name = "categoryid")
    @JsonIgnoreProperties({"item", "admin", "hibernateLazyInitializer"})
    private Category category;

    @OneToMany(mappedBy = "item")
    @JsonIgnore
    @ToString.Exclude
    private List<Feedback> feedback;

    @OneToMany(mappedBy = "item")
    @JsonIgnore
    @ToString.Exclude
    private List<OrderItem> orderItems;

}
