package com.zepox.EcommerceWebApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "shippingdetails")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingDetails extends BaseEntity{
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
    @JsonIgnore
    @ToString.Exclude
    private List<Order> orders;

}
