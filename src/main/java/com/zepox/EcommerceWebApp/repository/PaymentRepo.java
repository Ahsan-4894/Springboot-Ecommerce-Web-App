package com.zepox.EcommerceWebApp.repository;

import com.zepox.EcommerceWebApp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, String> {


    Payment findByStripeSessionId(@Param("sessionId") String sessionId);

    Optional<Payment> findByOrderId(@Param("orderId") String orderId);
}
