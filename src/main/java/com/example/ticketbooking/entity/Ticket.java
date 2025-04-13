package com.example.ticketbooking.entity;

import com.example.ticketbooking.dto.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private String ticketNo;
    private User bookedBy;
    private String section;

}
