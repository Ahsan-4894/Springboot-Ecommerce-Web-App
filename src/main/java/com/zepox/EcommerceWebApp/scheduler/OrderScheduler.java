package com.zepox.EcommerceWebApp.scheduler;

import com.zepox.EcommerceWebApp.entity.Order;
import com.zepox.EcommerceWebApp.entity.type.OrderStatusType;
import com.zepox.EcommerceWebApp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderScheduler {
    private final OrderService orderService;

//    Just a simulation that every CONFIRMED order after certain time has been shipped.
    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void setOrderStatusToShipped(){
        List<Order> confirmedOrders = orderService.getOrdersByStatus(OrderStatusType.PAID);
        confirmedOrders.forEach(orderService::markOrderAsShipped);
    }

//    Just a simulation that every SHIPPRED order after certain time has been delivered.
    @Scheduled(cron = "0 */50 * * * *")
    @Transactional
    public void setOrderStatusToDelivered(){
        List<Order> shippedOrders = orderService.getOrdersByStatus(OrderStatusType.DELIVERED);
        shippedOrders.forEach(orderService::markOrderAsDelivered);
    }
}
