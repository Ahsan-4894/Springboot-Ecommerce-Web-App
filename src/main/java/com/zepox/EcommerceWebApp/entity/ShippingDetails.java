package com.zepox.EcommerceWebApp.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "shippingdetails")
public class ShippingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    @Column(name = "contactnumber")
    private String contactNumber;

    private String address;
    private String city;
    @Column(name = "postalcode")
    private String postalCode;

    @OneToMany(mappedBy = "shippingDetails")
    private List<Order> orders;

}
