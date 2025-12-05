package com.zepox.EcommerceWebApp.scheduler;

import com.zepox.EcommerceWebApp.entity.Order;
import com.zepox.EcommerceWebApp.entity.Payment;
import com.zepox.EcommerceWebApp.entity.Reservation;
import com.zepox.EcommerceWebApp.service.OrderService;
import com.zepox.EcommerceWebApp.service.PaymentService;
import com.zepox.EcommerceWebApp.service.ReservationService;
import com.zepox.EcommerceWebApp.util.InventoryUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {
    private final InventoryUtil inventoryUtil;
    private final ReservationService reservationService;
    private final OrderService orderService;
    private final PaymentService paymentService;


    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void scheduleReservationCleanup() {
        List<Reservation> expiredReservations = reservationService.getExpiredReservations();
        for(Reservation r : expiredReservations) {

            Order order = r.getOrder();

            // 1. Release reserved stock
            inventoryUtil.updateReservedStockAfterPaymentFail(order.getStripeSessionId(), order.getId());

            // 2. Update reservation
            reservationService.markReservationAsExpired(r);

            // 3. Update order
            orderService.markOrderAsCancelled(order);

            // 4. Update payment
            Payment p = paymentService.getPaymentFromOrderId(order.getId());
            paymentService.markPaymentAsFailed(p);

        }
    }
}
