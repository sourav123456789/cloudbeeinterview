package com.example.ticketbooking.service;

import com.example.ticketbooking.dto.*;
import com.example.ticketbooking.exceptionhandler.TicketNotAvaiableExecption;
import com.example.ticketbooking.repositry.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public Receipt bookTicket(TicketRequest ticketRequest) {
        User user = new User(ticketRequest.getFirstName(), ticketRequest.getLastName()
                , ticketRequest.getEmail());
        Optional<TicketBooked> ticketBooked = ticketRepository.bookTicket(user);
        if(ticketBooked.isEmpty()) {
           throw new TicketNotAvaiableExecption("No tickets are available");
        }

        Receipt receipt = new Receipt();
        receipt.setUser(user);
        receipt.setReceipt(ticketBooked.get());
        return receipt;
    }

    public BookedSeatBySection getBookedTicketBySectionId(String sectionId) {
        Optional<BookedSeatBySection> bookedTicketBySection = ticketRepository.getBookedTicketBySection(sectionId);
        if(bookedTicketBySection.isPresent()) {
            return bookedTicketBySection.get();
        }
        BookedSeatBySection bookedSeatBySection = new BookedSeatBySection();
        bookedSeatBySection.setSeatAcquired(Collections.emptyList());
        return bookedSeatBySection;
    }

    public void deleteTicketForUser(String userMail) {
          ticketRepository.deleteUserByMail(userMail);
    }

    public UpdateSeatRequestResponse bookSpecificTicket(UpdateSeatRequestResponse updateSeat) {
        return ticketRepository.bookTicketBySeatNo(updateSeat);
    }
}
