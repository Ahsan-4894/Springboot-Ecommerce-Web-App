package com.zepox.EcommerceWebApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zepox.EcommerceWebApp.entity.type.PaymentStatusType;
import com.zepox.EcommerceWebApp.entity.type.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.LifecycleState;

import java.util.List;

@Entity
@Table(name = "payment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Payment extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private String paymentId;

    @OneToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    @ToString.Exclude
    private Order order;

    @ManyToOne
    @JoinColumn(name = "payer_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @Column(name = "payment_amount")
    private long paymentAmount;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatusType paymentStatus;

    @Column(name = "stripe_session_id")
    private String stripeSessionId;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;
}
