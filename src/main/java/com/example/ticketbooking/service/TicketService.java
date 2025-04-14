package com.example.ticketbooking.service;

import com.example.ticketbooking.dto.*;
import com.example.ticketbooking.entity.Ticket;
import com.example.ticketbooking.exceptionhandler.InvalidTicketDetailsException;
import com.example.ticketbooking.exceptionhandler.TicketNotAvaiableExecption;
import com.example.ticketbooking.repositry.TicketRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

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

    public void deleteTicketForUser(User user) {
          ticketRepository.deleteUserByMail(user);
    }

    public UpdateSeatRequestResponse bookSpecificTicket(UpdateSeatRequestResponse updateSeat) {
        Ticket ticket = new Ticket(updateSeat.getInfo().getTicketNo(), updateSeat.getUser() , updateSeat.getInfo().getSection());
        if(ticketRepository.isTicketAvailable(ticket)) {
            ticketRepository.bookTicketBySeatNo(ticket , updateSeat.getUser());
            return updateSeat;
        }
        throw new TicketNotAvaiableExecption("Requested Ticket is not available");

    }

    public Set<TicketBooked> replaceTicket(@Valid ReplaceTicketRequest replaceTicketRequest) {
        boolean booked = ticketRepository.hasBooked(replaceTicketRequest.getUser(),
                replaceTicketRequest.getBooked());
        if(!booked) {
            throw new InvalidTicketDetailsException("User Has not booked the ticket");
        }
        Ticket replaceTicket = new Ticket(replaceTicketRequest.getReplace().getTicketNo(),
         replaceTicketRequest.getUser()
        ,replaceTicketRequest.getReplace().getSection() );
        Ticket bookedticket = new Ticket(
                replaceTicketRequest.getBooked().getTicketNo(),
                replaceTicketRequest.getUser(),
                replaceTicketRequest.getBooked().getSection()
        );
        boolean isTicketAvailable = ticketRepository.isTicketAvailable(replaceTicket);
        if(!isTicketAvailable) {
            throw new TicketNotAvaiableExecption("Ticket not available");
        }


        Optional<Set<TicketBooked>> ticketBookeds = ticketRepository.replaceTicket(replaceTicketRequest.getUser(),
                bookedticket, replaceTicket);

        return ticketBookeds.get();

    }
}
