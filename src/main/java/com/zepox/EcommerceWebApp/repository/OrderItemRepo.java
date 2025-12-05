package com.zepox.EcommerceWebApp.repository;

import com.zepox.EcommerceWebApp.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepo extends JpaRepository<OrderItem, String> {

    @Query("""
    SELECT I FROM OrderItem I WHERE I.order.id = :orderId\s
   \s""")
    List<OrderItem> findAllItemsInByOrderId(@Param("orderId") String orderId);

    void deleteOrderItemsByOrderId(String orderId);

    Optional<List<OrderItem>> findOrderItemsByOrderId(String orderId);

    @Query("""
    SELECT OI FROM OrderItem OI
    WHERE OI.item.id = :itemId AND OI.order.id IN :orderIds
    """)
    Optional<List<OrderItem>> findOrderItemsByOrderIdAndItemId(
            @Param("orderIds") List<String> orderIds,
            @Param("itemId") String itemId
    );
}
