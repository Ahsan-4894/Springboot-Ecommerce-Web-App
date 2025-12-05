package com.zepox.EcommerceWebApp.repository;

import com.zepox.EcommerceWebApp.dto.response.Last7DaysOrdersResponseDto;
import com.zepox.EcommerceWebApp.dto.response.OrderStatsDto;
import com.zepox.EcommerceWebApp.entity.Order;
import com.zepox.EcommerceWebApp.entity.type.OrderStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, String> {
    @Query("""
        SELECT O FROM Order O
        WHERE O.user.id = :userId AND\s
        O.id = :orderId
        """)
    Optional<Order> findByIdAndUserid(@Param("userId") String userId, @Param("orderId") String orderId);

    @Query("""
        SELECT O FROM Order O
        WHERE O.user.id = :userId       \s
       \s""")
    List<Order> findByUserid(@Param("userId") String userId);

    @Modifying
    @Query("""
        UPDATE Order O
        SET O.status = :status
        WHERE O.id = :orderId
        """)
    void updateOrderStatus(@Param("orderId") String orderId, @Param("status") OrderStatusType status);

    Optional<Order> findByStripeSessionId(String stripeSessionId);

    @Query("""
          SELECT O FROM Order O
          WHERE O.status = :status
          """)
    Optional<List<Order>> findByStatus(@Param("status") OrderStatusType status);

    Optional<Order> getOrderById(@Param("orderId") String orderId);


    @Query("""
    SELECT O.id FROM Order O
    WHERE O.user.id = :userId
    AND O.status NOT IN ('PENDING_PAYMENT', 'CANCELLED')
    """)
    Optional<List<String>> findAllActiveOrdersOfAUser(@Param("userId") String userId);

//    @Query(value = """
//            SELECT\s
//                TO_CHAR(orderplaced, 'YYYY-MM-DD') AS orderplaced,
//                COUNT(*) AS totalOrdersPlaced,
//                SUM(price) AS totalRevenue
//            FROM orders
//            WHERE status IN ('PAID','SHIPPED', 'DELIVERED')
//            GROUP BY orderplaced
//            ORDER BY orderplaced DESC
//           \s""",
//            nativeQuery = true)
//    Optional<List<Last7DaysOrdersResponseDto>> getOrderStats();
}
