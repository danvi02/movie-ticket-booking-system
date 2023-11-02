package com.movie.ticket.booking.system.payment.service;

import com.movie.ticket.booking.system.commons.dto.BookingDTO;
import org.springframework.web.bind.annotation.RequestBody;

public interface PaymentService {
    public void processPayment(BookingDTO bookingDTO);
}

