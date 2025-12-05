package com.zepox.EcommerceWebApp.repository;

import com.zepox.EcommerceWebApp.entity.Reservation;
import com.zepox.EcommerceWebApp.entity.type.ReservationStatusType;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, String> {
    @Query("""
        SELECT R FROM Reservation R
        WHERE R.order.id = :orderId AND R.user.id = :userId
        """)
    Optional<Reservation> findByorderIdAnduserId(@Param("orderId") String orderId,
                                                 @Param("userId") String userId);

    @Query("""
        SELECT R FROM Reservation R
        WHERE R.status = :status AND R.expriresAt < :time
        """)
    Optional<List<Reservation>> findByStatusAndExpiresAtBefore(@Param("status") ReservationStatusType status,
                                                     @Param("time") LocalDateTime time);
}
