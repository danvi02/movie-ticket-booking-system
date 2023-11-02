package com.movie.ticket.booking.system.service.kafka.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.movie.ticket.booking.system.commons.dto.BookingDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceKafkaPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishToPaymentRequestTopic(BookingDTO bookingDTO){
        log.info("publishing booking details to the payment-request kafka topic");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            this.kafkaTemplate.send("payment-request",objectMapper.writeValueAsString(bookingDTO));
        } catch (JsonProcessingException e) {
            log.error("Error while publishing booking details to the payment-request kafka topic");
        }
    }
}
