package com.zepox.EcommerceWebApp.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.zepox.EcommerceWebApp.dto.request.CheckoutOrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StripeService {
    @Value("${stripe.api.secret-key}")
    private String secretKey;

    @Value("${frontend.url.payment-success}")
    private String FRONTEND_PAYMENT_SUCCESS_URL;

    @Value("${frontend.url.payment-fail}")
    private String FRONTEND_PAYMENT_FAIL_URL;


    @Transactional
    public Session createSessionWithStripe(CheckoutOrderRequestDto request) throws StripeException {
        Stripe.apiKey = secretKey;
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for(CheckoutOrderRequestDto.CartItem item : request.getCart().getCartItems()){

            String[] detailsPart = item.getDetails().split(",");
            String productName = detailsPart.length > 1 ? detailsPart[1] : "Unnamed Product";
            String imageUrl = detailsPart.length > 2 ? detailsPart[2] : null;
            SessionCreateParams.LineItem.PriceData.ProductData.Builder productDataBuilder =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(productName);

//            Optional image
            if (imageUrl != null) {
                productDataBuilder.addImage(imageUrl);
            }
            String currency = request.getCurrency() == null ? "USD" : request.getCurrency();
            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(currency)
                            .setUnitAmount((long) (item.getPrice() * 100))
                            .setProductData(productDataBuilder.build())
                            .build();

            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) item.getQuantity())
                            .setPriceData(priceData)
                            .build();

            lineItems.add(lineItem);
        }
        SessionCreateParams params = SessionCreateParams.builder()
                .setSuccessUrl(FRONTEND_PAYMENT_SUCCESS_URL)
                .setCancelUrl(FRONTEND_PAYMENT_FAIL_URL)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addAllLineItem(lineItems)
                .build();
        return Session.create(params);
    }
}
