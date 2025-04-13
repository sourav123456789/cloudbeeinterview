package com.example.ticketbooking.entity;

import com.example.ticketbooking.dto.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private String ticketNo;
    private User bookedBy;
    private String section;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(ticketNo, ticket.ticketNo) && Objects.equals(section, ticket.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketNo, section);
    }
}
