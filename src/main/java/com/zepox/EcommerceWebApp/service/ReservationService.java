package com.zepox.EcommerceWebApp.service;

import com.zepox.EcommerceWebApp.entity.Reservation;
import com.zepox.EcommerceWebApp.entity.type.ReservationStatusType;
import com.zepox.EcommerceWebApp.exception.custom.ReservationDoesNotExistException;
import com.zepox.EcommerceWebApp.repository.ReservationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepo reservationRepo;

    public void updateReservationStatusByOrderIdAndUserId(String orderId,String userId ,ReservationStatusType status){
        Reservation reservation = reservationRepo.findByorderIdAnduserId(orderId, userId).orElseThrow(()->new ReservationDoesNotExistException("Reservation doesn't exist"));
        reservation.setStatus(status);
        reservationRepo.save(reservation);
    }

    public void saveReservation(Reservation reservation){
        reservationRepo.save(reservation);
    }

    public List<Reservation> getExpiredReservations(){
        return reservationRepo.findByStatusAndExpiresAtBefore(
                ReservationStatusType.ACTIVE,
                LocalDateTime.now()
        ).orElseThrow(()-> new ReservationDoesNotExistException("No expired reservations found"));
    }
    public void markReservationAsExpired(Reservation reservation){
        reservation.setStatus(ReservationStatusType.EXPIRED);
        reservationRepo.save(reservation);
    }
}
