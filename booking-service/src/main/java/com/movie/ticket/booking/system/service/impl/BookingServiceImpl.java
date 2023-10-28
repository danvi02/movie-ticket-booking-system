package com.movie.ticket.booking.system.service.impl;

import com.movie.ticket.booking.system.commons.dto.BookingDTO;
import com.movie.ticket.booking.system.commons.dto.BookingStatus;
import com.movie.ticket.booking.system.service.BookingService;
import com.movie.ticket.booking.system.service.broker.PaymentServiceBroker;
import com.movie.ticket.booking.system.service.entity.BookingEntity;
import com.movie.ticket.booking.system.service.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingServiceImpl implements BookingService {

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
        // call payment service
        bookingDTO = this.paymentServiceBroker.makePayment(bookingDTO); // Kafka -> ensures that even if the payment service is not up and running,
        // It will store the requests data in the Kafka topics in sequential order. Lets say if the payment service is UP after some time,
        //Then requests available in Kafka topic will be taken by payment service to process the requests.(5, 10 , 20) reloading icon
        bookingEntity.setBookingStatus(bookingDTO.getBookingStatus());
        return BookingDTO.builder()
                .bookingId(bookingEntity.getBookingId())
                .bookingAmount(bookingEntity.getBookingAmount())
                .bookingStatus(bookingEntity.getBookingStatus())
                .movieId(bookingEntity.getMovieId())
                .showTime(bookingEntity.getShowTime())
                .showDate(bookingEntity.getShowDate())
                .bookingAmount(bookingEntity.getBookingAmount())
                .userId(bookingEntity.getUserId())
                .seatsBooked(bookingEntity.getSeatsBooked())
                .build();
    }
}
