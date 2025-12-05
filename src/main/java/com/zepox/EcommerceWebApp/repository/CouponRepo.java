package com.zepox.EcommerceWebApp.repository;

import com.zepox.EcommerceWebApp.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepo extends JpaRepository<Coupon,String> {
}
