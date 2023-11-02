package com.movie.ticket.booking.system.payment.service.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.movie.ticket.booking.system.commons.dto.BookingDTO;
import com.movie.ticket.booking.system.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceKafkaListener {

    private final PaymentService paymentService;

    @KafkaListener(topics = "payment-request", groupId = "paymentrequest")
    public void pullFromPaymentRequestTopic(String bookingDTOJson){
        log.info("received booking details from the payment-request kafka topic in payment-service");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            BookingDTO bookingDTO = objectMapper.readValue(bookingDTOJson, BookingDTO.class);
            this.paymentService.processPayment(bookingDTO);
        } catch (JsonProcessingException e) {
            log.error("error while receiving the booking details from the " +
                    "payment-request kafka topic in payment-service");
        }

    }
}
