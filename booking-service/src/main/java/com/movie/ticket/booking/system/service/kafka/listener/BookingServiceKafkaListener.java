package com.movie.ticket.booking.system.service.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.movie.ticket.booking.system.commons.dto.BookingDTO;
import com.movie.ticket.booking.system.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookingServiceKafkaListener {

    @Autowired
    private BookingService bookingService;

    @KafkaListener(topics = "payment-response", groupId = "paymentresponse1")
    public void receiveFromPaymentResponseTopic(String bookingDTOJson){
        log.info("Received confirmation on booking details from payment-response kafka topic");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            BookingDTO bookingDTO = objectMapper.readValue(bookingDTOJson, BookingDTO.class);
            this.bookingService.processBooking(bookingDTO);
        } catch (JsonProcessingException e) {
            log.error("Error while receiving confirmation on booking details from " +
                    "payment-response kafka topic");
        }

    }
}
