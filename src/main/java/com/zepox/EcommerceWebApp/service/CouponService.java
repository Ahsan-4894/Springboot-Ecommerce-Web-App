package com.zepox.EcommerceWebApp.service;

import com.zepox.EcommerceWebApp.dto.response.ApplyCouponResponseDto;
import com.zepox.EcommerceWebApp.entity.Coupon;
import com.zepox.EcommerceWebApp.exception.custom.CouponAlreadyUsedException;
import com.zepox.EcommerceWebApp.exception.custom.CouponDoesNotExistException;
import com.zepox.EcommerceWebApp.repository.CouponRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class CouponService {
    private final CouponRepo couponRepo;

    public ApplyCouponResponseDto applyCoupon(String couponCode) {
//        What if a coupon code is invalid.
        Coupon coupon = couponRepo.findById(couponCode).orElseThrow(()-> new CouponDoesNotExistException("Invalid Coupon Code"));

//        What if this coupon has already been availed.
        if(coupon.getIsUsed() == 1) throw new CouponAlreadyUsedException("Coupon already used");

//        Now update coupon (set isUsed to 1)
        coupon.setIsUsed(1);

        couponRepo.save(coupon);

        return ApplyCouponResponseDto.builder()
                .success(true)
                .message("Coupon Applied")
                .discountRate(String.valueOf(coupon.getDiscountRate()))
                .build();
    }
}
