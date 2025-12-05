package com.zepox.EcommerceWebApp.controller.Public;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.zepox.EcommerceWebApp.config.StripeConfig;
import com.zepox.EcommerceWebApp.service.StripeWebhookService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/public/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {
    private final StripeConfig stripeConfig;
    private final StripeWebhookService stripeWebhookService;
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(HttpServletRequest request,
                                                @RequestHeader("Stripe-Signature") String sigHeader
                                                ) throws IOException, SignatureVerificationException {
        String payload;
        try{
            payload = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        }catch(IOException e){
            throw new IOException(e);
        }
        Event event = null;
        try{
            event = Webhook.constructEvent(payload, sigHeader, stripeConfig.getWebhookSecret());
        }catch(SignatureVerificationException e){
//            Handler error here.
            throw new SignatureVerificationException(e.getMessage(), sigHeader);
        }
        stripeWebhookService.handleEvent(event);
        return ResponseEntity.ok().body("Webhook received!");
    }
}
