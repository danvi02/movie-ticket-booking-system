package com.movie.ticket.booking.system.payment.service.impl;

import com.movie.ticket.booking.system.commons.constants.LoggerConstants;
import com.movie.ticket.booking.system.commons.dto.BookingDTO;
import com.movie.ticket.booking.system.commons.dto.BookingStatus;
import com.movie.ticket.booking.system.payment.service.PaymentService;
import com.movie.ticket.booking.system.payment.service.dto.PaymentDto;
import com.movie.ticket.booking.system.payment.service.entity.PaymentEntity;
import com.movie.ticket.booking.system.payment.service.entity.PaymentStatus;
import com.movie.ticket.booking.system.payment.service.kafka.publisher.PaymentServiceKafkaPublisher;
import com.movie.ticket.booking.system.payment.service.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentServiceKafkaPublisher paymentServiceKafkaPublisher;

    @Autowired
    private StripePaymentGateway stripePaymentGateway;

    @Autowired
    private PaymentRepository paymentRepository;



    @Override
    @Transactional
    public void processPayment(BookingDTO bookingDTO) {
        log.info("received booking details in payment service with data {}", bookingDTO.toString());
        log.info(LoggerConstants.ENTERED_SERVICE_MESSAGE.getValue(), "createPayment", this.getClass());
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .paymentStatus(PaymentStatus.PENDING)
                .paymentAmount(bookingDTO.getBookingAmount())
                .bookingId(bookingDTO.getBookingId())
                .build();
        this.paymentRepository.save(paymentEntity);
        this.stripePaymentGateway.makePayment(bookingDTO);
        paymentEntity.setPaymentTimestamp(LocalDateTime.now());
        if(bookingDTO.getBookingStatus().equals(BookingStatus.CONFIRMED)){
            paymentEntity.setPaymentStatus(PaymentStatus.APPROVED);
        }else{
            paymentEntity.setPaymentStatus(PaymentStatus.FAILED);
            bookingDTO.setBookingStatus(BookingStatus.CANCELLED);
        }
        this.paymentServiceKafkaPublisher.pushDataToPaymentResponseTopic(bookingDTO);
    }
}
