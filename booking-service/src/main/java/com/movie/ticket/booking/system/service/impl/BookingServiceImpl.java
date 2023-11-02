package com.movie.ticket.booking.system.service.impl;

import com.movie.ticket.booking.system.commons.dto.BookingDTO;
import com.movie.ticket.booking.system.commons.dto.BookingStatus;
import com.movie.ticket.booking.system.service.BookingService;
import com.movie.ticket.booking.system.service.broker.PaymentServiceBroker;
import com.movie.ticket.booking.system.service.entity.BookingEntity;
import com.movie.ticket.booking.system.service.kafka.publisher.BookingServiceKafkaPublisher;
import com.movie.ticket.booking.system.service.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingServiceKafkaPublisher bookingServiceKafkaPublisher;

    @Autowired
    private PaymentServiceBroker paymentServiceBroker;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        BookingEntity bookingEntity = BookingEntity.builder()
                .bookingAmount(bookingDTO.getBookingAmount())
                .seatsBooked(bookingDTO.getSeatsBooked())
                .bookingStatus(BookingStatus.PENDING)
                .movieId(bookingDTO.getMovieId())
                .userId(bookingDTO.getUserId())
                .showDate(bookingDTO.getShowDate())
                .showTime(bookingDTO.getShowTime())
                .build();
        this.bookingRepository.save(bookingEntity); // create a booking with booking status as PENDING
        bookingDTO.setBookingId(bookingEntity.getBookingId());
        bookingDTO.setBookingStatus(BookingStatus.PENDING);
        //Publish the booking to kafka topic
        this.bookingServiceKafkaPublisher.publishToPaymentRequestTopic(bookingDTO);
        return bookingDTO;
    }

    @Override
    @Transactional
    public void processBooking(BookingDTO bookingDTO) {
        Optional<BookingEntity> bookingEntityOptional= this.bookingRepository.findById(bookingDTO.getBookingId());
        if(bookingEntityOptional.isPresent()){
            BookingEntity bookingEntity = bookingEntityOptional.get();
            bookingEntity.setBookingStatus(bookingDTO.getBookingStatus());
        }

    }
}
