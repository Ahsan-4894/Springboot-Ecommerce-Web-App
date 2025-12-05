package com.zepox.EcommerceWebApp.controller.User;


import com.zepox.EcommerceWebApp.dto.response.ApplyCouponResponseDto;
import com.zepox.EcommerceWebApp.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/coupon")
@RequiredArgsConstructor
public class UserCouponController {
    private final CouponService couponService;
    @PostMapping("/apply")
    public ResponseEntity<ApplyCouponResponseDto> applyCoupon(@Validated @RequestBody String couponCode){
        return ResponseEntity.ok().body(couponService.applyCoupon(couponCode));
    }
}
