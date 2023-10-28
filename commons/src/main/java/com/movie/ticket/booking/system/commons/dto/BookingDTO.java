package com.movie.ticket.booking.system.commons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class BookingDTO {
    private UUID bookingId;
    @NotBlank(message = "user id is mandatory and it cannot be blank")
    private String userId;
    @NotNull(message = "movie id is mandatory")
    private Integer movieId;
    @NotNull(message = "user must have booked at least 1 seat")
    private List<String> seatsBooked;
    @NotNull(message = "show date Is mandatory")
    private LocalDate showDate;
    @NotNull(message = "show time Is mandatory")
    private LocalTime showTime;
    private BookingStatus bookingStatus;
    @NotNull(message = "booking amount is mandatory")
    @Positive(message = "amount must be greater than zero")
    private Double bookingAmount;
}
