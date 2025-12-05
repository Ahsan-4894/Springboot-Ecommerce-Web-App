package com.zepox.EcommerceWebApp.service;

import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.zepox.EcommerceWebApp.entity.Order;
import com.zepox.EcommerceWebApp.entity.type.OrderStatusType;
import com.zepox.EcommerceWebApp.entity.type.PaymentStatusType;
import com.zepox.EcommerceWebApp.entity.type.ReservationStatusType;
import com.zepox.EcommerceWebApp.util.InventoryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StripeWebhookService {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final InventoryUtil inventoryUtil;
    private final ReservationService reservationService;
    private final OrderItemService orderItemService;

    public void handleEvent(Event event) {
        String eventType = event.getType();
        switch (eventType) {
            case "checkout.session.completed":
                handleCheckoutSessionCompleted(event);
                break;

            case "payment_intent.succeeded":
                handlePaymentSucceeded(event);
                break;

            case "payment_intent.payment_failed":
                handlePaymentFailed(event);
                break;

            default:
                System.out.println("Unhandled event type: " + eventType);
                break;
        }
    }
    private void handleCheckoutSessionCompleted(Event event) {

        System.out.println("Checkout session completed, Strip Webhook");
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if(dataObjectDeserializer.getObject().isPresent()){
            Session session = (Session) dataObjectDeserializer.getObject().get();
            String sessionId = session.getId();
            String paymentIntentId = session.getPaymentIntent();

//            Update Order + Payment status + Reservation Status
            Order order = orderService.updateOrderStatusBySessionId(sessionId, OrderStatusType.PAID);
            paymentService.updateStatusAfterPayment(sessionId, PaymentStatusType.SUCCEEDED, paymentIntentId);
            reservationService.updateReservationStatusByOrderIdAndUserId(
                    order.getId(),
                    order.getUser().getId(),
                    ReservationStatusType.CONFIRMED
            );

//            Update reserved stock (reduce reserved stock and actual quantity)
            String orderIdCorrespondingToSessionId = orderService.getOrderIdBySessionId(sessionId);
            inventoryUtil.updateReservedStockAfterPaymentSuccessful(sessionId, orderIdCorrespondingToSessionId);

            System.out.println("Checkout session completed: " + sessionId);
        }
    }
    private void handlePaymentSucceeded(Event event) {
        System.out.println("Checkout payment succeeded, Strip Webhook");
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if(dataObjectDeserializer.getObject().isPresent()){
            Session session = (Session) dataObjectDeserializer.getObject().get();
            String paymentIntentId = session.getPaymentIntent();
            paymentService.updateStatusAfterPayment(paymentIntentId, PaymentStatusType.SUCCEEDED, paymentIntentId);
            System.out.println("Payment succeeded for: " + session.getId());
        }
    }

    private void handlePaymentFailed(Event event) {
        System.out.println("Checkout payment failed, Strip Webhook");
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (dataObjectDeserializer.getObject().isPresent()) {

            Session session = (Session) dataObjectDeserializer.getObject().get();
            String sessionId = session.getId();
            String paymentIntentId = session.getPaymentIntent();

//            Update Order + Payment status + Reservation Status + OrderItems table as well.
            Order order = orderService.updateOrderStatusBySessionId(sessionId, OrderStatusType.CANCELLED);
            paymentService.updateStatusAfterPayment(sessionId, PaymentStatusType.FAILED, paymentIntentId);
            reservationService.updateReservationStatusByOrderIdAndUserId(
                    order.getId(),
                    order.getUser().getId(),
                    ReservationStatusType.RELEASED
            );
            orderItemService.removeOrderItems(order.getId());

//          Update reserved stock (reduce reserved stock)
            String orderIdCorrespondingToSessionId = orderService.getOrderIdBySessionId(sessionId);
            inventoryUtil.updateReservedStockAfterPaymentFail(sessionId, orderIdCorrespondingToSessionId);
            System.out.println("Payment failed for: " + paymentIntentId);
        }
    }
}
