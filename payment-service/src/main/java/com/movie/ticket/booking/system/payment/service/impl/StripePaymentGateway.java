package com.movie.ticket.booking.system.payment.service.impl;

import com.movie.ticket.booking.system.commons.dto.BookingDTO;
import com.movie.ticket.booking.system.commons.dto.BookingStatus;
import com.movie.ticket.booking.system.payment.service.entity.PaymentStatus;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class StripePaymentGateway {

    @Value("${stripe.key}")
    private String secretKey;

    @PostConstruct
    public void init(){
        Stripe.apiKey = secretKey;
    }

    public void makePayment(BookingDTO bookingDTO){
        try {
//            PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
//                    .setAmount(bookingDTO.getBookingAmount().longValue())
//                    .setCurrency("usd")
//                    .setPaymentMethod("pm_card_in")
//                    .build();
//            PaymentIntent paymentIntent = PaymentIntent.create(params);
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", (int)Math.round(bookingDTO.getBookingAmount()));
            chargeParams.put("currency", "inr");
            chargeParams.put("description", "Test Payment");
            chargeParams.put("source", "tok_visa");
            Charge.create(chargeParams);
            bookingDTO.setBookingStatus(BookingStatus.CONFIRMED);
        }catch(Exception e){
            log.error("Payment failed at gateway level with exception : "+ e.getMessage());
        }
    }

}
