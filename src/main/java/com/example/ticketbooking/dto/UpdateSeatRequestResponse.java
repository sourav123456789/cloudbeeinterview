package com.example.ticketbooking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateSeatRequestResponse {
    @NotNull(message = "User cannot be null")
    private User user;

    @NotNull(message = "TicketInfo cannot be null")
    private TicketInfo info;
}
