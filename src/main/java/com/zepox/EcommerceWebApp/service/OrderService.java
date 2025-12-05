package com.zepox.EcommerceWebApp.service;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.zepox.EcommerceWebApp.dto.request.CheckoutOrderRequestDto;
import com.zepox.EcommerceWebApp.dto.request.UpdateOrderStatusRequestDto;
import com.zepox.EcommerceWebApp.dto.response.*;
import com.zepox.EcommerceWebApp.entity.*;
import com.zepox.EcommerceWebApp.entity.type.OrderStatusType;
import com.zepox.EcommerceWebApp.entity.type.PaymentStatusType;
import com.zepox.EcommerceWebApp.entity.type.PaymentType;
import com.zepox.EcommerceWebApp.entity.type.ReservationStatusType;
import com.zepox.EcommerceWebApp.exception.custom.OrderDoesNotExistException;
import com.zepox.EcommerceWebApp.exception.custom.UserDoesNotExistException;
import com.zepox.EcommerceWebApp.mapper.OrderMapper;
import com.zepox.EcommerceWebApp.repository.*;
import com.zepox.EcommerceWebApp.util.AuthContext;
import com.zepox.EcommerceWebApp.util.InventoryUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final AuthContext authContext;
    private final OrderRepo orderRepo;
    private final OrderMapper orderMapper;
    private final StripeService stripeService;
    private final InventoryUtil inventoryUtil;
    private final ShippingDetailsService shippingDetailsService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final OrderItemService orderItemService;


    public GetOrderByUserIdResponseDto getOrderByUserId(String orderId) {
        String userId = authContext.getIdOfCurrentLoggedInUser();
        Order order = orderRepo.findByIdAndUserid(userId, orderId).orElseThrow(() -> new OrderDoesNotExistException(orderId));

        OrdersResponseDto transformedOrder = orderMapper.toDto(order, 0);
        return GetOrderByUserIdResponseDto.builder()
                .success(true)
                .message(transformedOrder)
                .build();

    }

    public GetOrdersByUserIdResponseDto getOrdersByUserId() {
        String userId = authContext.getIdOfCurrentLoggedInUser();
        List<Order> orders = orderRepo.findByUserid(userId);
        if(orders==null || orders.isEmpty()) throw new OrderDoesNotExistException("No orders!");
        List<OrdersResponseDto> transformedOrders = orderMapper.toDtos(orders);
        return GetOrdersByUserIdResponseDto.builder()
                .success(true)
                .message(transformedOrders)
                .build();
    }

    public void updateOrderStatus(UpdateOrderStatusRequestDto dto) {
        Order order = orderRepo.findById(dto.orderId()).orElse(null);
        if(order == null) throw new OrderDoesNotExistException("No orders!");
        if(dto.status() == OrderStatusType.DELIVERED) throw new OrderDoesNotExistException("Order Status Already Updated!");
        orderRepo.updateOrderStatus(
                dto.orderId(),
                dto.status()
        );
    }

    @Transactional
    public StripeResponse checkout(CheckoutOrderRequestDto dto) throws StripeException {
        String loggedUserId = authContext.getCurrentLoggedInUser().getUser().getId();
        User user = userService.findUserById(loggedUserId);

        List<String> allRequestedItemIds = new ArrayList<>();
        List<Integer> allRequestedQuantity = new ArrayList<>();
        for(var cartItem :  dto.getCart().getCartItems()){
            String[] details = cartItem.getDetails().split(",");
            allRequestedItemIds.add(details[0]);
            allRequestedQuantity.add(cartItem.getQuantity());
        }

        inventoryUtil.checkAvaliabilityOfStocks(allRequestedItemIds, allRequestedQuantity);

        ShippingDetails shippingDetails = ShippingDetails.builder()
                .name(dto.getShippingDetails().getName())
                .contactNumber(dto.getShippingDetails().getContactNumber())
                .address(dto.getShippingDetails().getAddress())
                .city(dto.getShippingDetails().getCity())
                .postalCode(dto.getShippingDetails().getPostalCode())
                .build();
        shippingDetailsService.saveShippingDetails(shippingDetails);

        Session session = stripeService.createSessionWithStripe(dto);

        Order order = Order.builder()
                .user(user)
                .shippingDetails(shippingDetails)
                .price(dto.getOrderDetails().getPrice())
                .status(OrderStatusType.PENDING_PAYMENT)
                .orderPlaced(LocalDateTime.now())
                .stripeSessionId(session.getId())
                .build();
        orderRepo.save(order);

        orderItemService.addOrderItems(
                order,
                allRequestedItemIds,
                allRequestedQuantity
        );

        Reservation reservation = Reservation.builder()
                .user(user)
                .order(order)
                .status(ReservationStatusType.ACTIVE)
                .expriresAt(LocalDateTime.now().plusMinutes(15))
                .build();
        reservationService.saveReservation(reservation);


        Map<String, Integer> itemsMap = IntStream.range(0, allRequestedItemIds.size())
                        .boxed()
                        .collect(Collectors.toMap(
                                allRequestedItemIds::get,
                                allRequestedQuantity::get
                        ));
        inventoryUtil.increaseReservedStockBulk(itemsMap);

        Payment payment = Payment.builder()
                .order(order)
                .user(user)
                .paymentAmount((long) order.getPrice())
                .paymentType(PaymentType.STRIPE)
                .paymentStatus(PaymentStatusType.PENDING)
                .stripeSessionId(session.getId())
                .paymentIntentId(null)
                .build();
        paymentService.savePayment(payment);


        return StripeResponse.builder()
                .message("Stripe Session Created!")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }

    public Order updateOrderStatusBySessionId(String sessionId, OrderStatusType status) {
        Order order = orderRepo.findByStripeSessionId(sessionId).orElseThrow(()-> new OrderDoesNotExistException("Order not found for session:" + sessionId));
        order.setStatus(status);
        return orderRepo.save(order);
    }

    public String getOrderIdBySessionId(String sessionId){
        Order order = orderRepo.findByStripeSessionId(sessionId).orElseThrow(()-> new OrderDoesNotExistException("Order not found for session:" + sessionId) );
        return order.getId();
    }

    public List<Order> getOrdersByStatus(OrderStatusType status){
        return orderRepo.findByStatus(status).orElseThrow(()-> new OrderDoesNotExistException("No confirmed orders"));
    }

    public void markOrderAsShipped(Order order){
        order.setStatus(OrderStatusType.SHIPPED);
        orderRepo.save(order);
    }

    public void markOrderAsDelivered(Order order){
        order.setStatus(OrderStatusType.DELIVERED);
        orderRepo.save(order);
    }

    public void markOrderAsCancelled(Order order){
        order.setStatus(OrderStatusType.CANCELLED);
        orderRepo.save(order);
    }

    public Order getOrderById(String orderId){
        return orderRepo.getOrderById(orderId).orElseThrow(()-> new OrderDoesNotExistException("Order not found for id:" + orderId));
    }

    public List<String> getConfirmedOrderIdsOfAUser(String userId){
        return orderRepo.findAllActiveOrdersOfAUser(userId).orElseThrow(()-> new OrderDoesNotExistException("Orders not found for thi userId:" + userId));
    }

    public List<Order> getLast4DaysOrderPlaced() {
        return null;
    }

    public OrdersResponseDto searchOrders() {
        return null;
    }

    public OrdersResponseDto getOrderDetails(String orderId, String userId) {
        return null;
    }

    public OrdersResponseDto getOrders() {
        return null;
    }

//    public List<Last7DaysOrdersResponseDto> getLast7DaysOrderPlaced() {
//        return orderRepo.getOrderStats().orElseThrow(()-> new OrderDoesNotExistException("No orders placed yet!"));
//    }
}
