package com.zepox.EcommerceWebApp.service;

import com.zepox.EcommerceWebApp.entity.Payment;
import com.zepox.EcommerceWebApp.entity.type.PaymentStatusType;
import com.zepox.EcommerceWebApp.exception.custom.PaymentDoesNotExistException;
import com.zepox.EcommerceWebApp.repository.PaymentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepo paymentRepo;
    public void updateStatusAfterPayment(String sessionId, PaymentStatusType paymentStatus, String paymentIntentId){
        Payment payment = paymentRepo.findByStripeSessionId(sessionId);
        if(payment == null) throw new PaymentDoesNotExistException("Payment not found: " + sessionId);
        payment.setPaymentStatus(paymentStatus);
        payment.setPaymentIntentId(paymentIntentId);
        paymentRepo.save(payment);
    }
    public void savePayment(Payment payment){
        paymentRepo.save(payment);
    }

    public Payment getPaymentFromOrderId(String orderId){
        return paymentRepo.findByOrderId(orderId).orElseThrow(() -> new PaymentDoesNotExistException("Payment not found: " + orderId));
    }

    public void markPaymentAsFailed(Payment payment){
        payment.setPaymentStatus(PaymentStatusType.FAILED);
        paymentRepo.save(payment);
    }
}
