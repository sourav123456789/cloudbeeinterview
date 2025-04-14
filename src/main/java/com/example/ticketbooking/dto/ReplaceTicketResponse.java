package com.example.ticketbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplaceTicketResponse {

    private User user;
    private Set<TicketBooked> receipts;
}
