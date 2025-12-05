package com.zepox.EcommerceWebApp.repository;

import com.zepox.EcommerceWebApp.entity.ShippingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepo extends JpaRepository<ShippingDetails, String> {
}
