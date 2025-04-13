package com.example.ticketbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TicketBooked {
    private String ticketNo;

    public TicketBooked() {

    }

    public TicketBooked(String ticketNo, String section, double price) {
        this.ticketNo = ticketNo;
        this.section = section;
        this.price = price;
    }

    private String section;
    private double price;
    private String from = "LONDON";
    private String to = "FRANCE";
}
