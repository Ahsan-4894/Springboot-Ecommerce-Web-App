package com.zepox.EcommerceWebApp.entity;

import com.zepox.EcommerceWebApp.entity.type.PaymentStatusType;
import com.zepox.EcommerceWebApp.entity.type.PaymentType;
import jakarta.persistence.*;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private String paymentId;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne
    @JoinColumn(name = "payer_id")
    private User user;

    @Column(name = "payment_amount")
    private long paymentAmount;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatusType paymentStatus;

    @Column(name = "payment_type")
    private PaymentType paymentType;

}
