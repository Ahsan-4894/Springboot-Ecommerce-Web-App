package com.zepox.EcommerceWebApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zepox.EcommerceWebApp.entity.type.ReservationStatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @OneToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    @ToString.Exclude
    private Order order;

    @Enumerated(EnumType.STRING)
    private ReservationStatusType status;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expriresAt;

}
