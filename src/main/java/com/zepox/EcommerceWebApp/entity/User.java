package com.zepox.EcommerceWebApp.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(name = "phonenumber")
    private String phoneNumber;
    private String role;


    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private List<Order> orders;


    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private List<Payment> payment;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private List<Feedback> feedback;

    @OneToMany(mappedBy = "admin")
    @JsonIgnore
    @ToString.Exclude
    private List<Coupon> coupons;

    @OneToMany(mappedBy = "admin")
    @JsonIgnore
    @ToString.Exclude
    private List<Category> categories;

    @OneToMany(mappedBy = "admin")
    @JsonIgnore
    @ToString.Exclude
    private List<Item> items;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private List<Reservation> reservation;

}
