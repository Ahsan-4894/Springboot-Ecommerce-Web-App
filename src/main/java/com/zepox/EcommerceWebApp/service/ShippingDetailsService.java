package com.zepox.EcommerceWebApp.service;

import com.zepox.EcommerceWebApp.entity.ShippingDetails;
import com.zepox.EcommerceWebApp.repository.ShippingRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingDetailsService {
    private final ShippingRepo shippingRepo;

    public void saveShippingDetails(ShippingDetails shippingDetails) {
        shippingRepo.save(shippingDetails);
    }
}
