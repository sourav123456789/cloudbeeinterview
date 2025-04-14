package com.example.ticketbooking.dto;

import com.example.ticketbooking.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReplaceTicketRequest {

    private User user;

    private TicketInfo booked;

    private TicketInfo replace;
}
