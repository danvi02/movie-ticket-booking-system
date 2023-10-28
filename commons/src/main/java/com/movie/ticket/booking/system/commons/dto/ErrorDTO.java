package com.movie.ticket.booking.system.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ErrorDTO {
    private String code;
    private List<String> errorMessages;
}
