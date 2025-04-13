package com.example.ticketbooking.service;

import com.example.ticketbooking.dto.TicketBooked;
import com.example.ticketbooking.repositry.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final TicketRepository ticketRepository;


    public Set<TicketBooked> getReceipt(String userName) {
        Optional<Set<TicketBooked>> receipt = ticketRepository.getReceipt(userName);
        return receipt.orElse(Collections.emptySet());
    }


}
