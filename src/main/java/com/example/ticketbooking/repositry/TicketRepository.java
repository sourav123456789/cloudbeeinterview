package com.example.ticketbooking.repositry;

import com.example.ticketbooking.dto.*;
import com.example.ticketbooking.entity.Ticket;
import com.example.ticketbooking.exceptionhandler.TicketNotAvaiableExecption;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TicketRepository {

    private static final Map<String, Set<Ticket>> availableTicketsBySection = new HashMap<>();

    private static final Map<String, Set<Ticket>> bookedTicketsBySection = new HashMap<>();

    private Map<String, Set<TicketBooked>> bookedTicketsByuser = new HashMap<>();

    static {
        Set<Ticket> t1 = new HashSet<>();
        Set<Ticket> t2 = new HashSet<>();

        for (int i = 1; i <= 1000; i++) {
            t1.add(new Ticket("A" + i, null, "A"));
            t2.add(new Ticket("B" + i, null, "B"));
        }
        availableTicketsBySection.put("A", t1);
        availableTicketsBySection.put("B", t2);
    }


    // This method will book a ticket from one of the section based on availability
    public Optional<TicketBooked> bookTicket(User user) {
        TicketBooked ticketBooked = null;
        for (Map.Entry<String, Set<Ticket>> entry : availableTicketsBySection.entrySet()) {
            Set<Ticket> value = entry.getValue();
            if (value.isEmpty()) continue;
            Ticket t = value.stream().findFirst().get();
            ticketBooked = new TicketBooked();
            ticketBooked.setTicketNo(t.getTicketNo());
            ticketBooked.setSection(entry.getKey());
            ticketBooked.setPrice(20);

            if (bookedTicketsByuser.containsKey(user.getEmail())) {
                Set<TicketBooked> ticketBook = bookedTicketsByuser.get(user.getEmail());
                ticketBook.add(ticketBooked);
            } else {
                Set<TicketBooked> s = new HashSet<>();
                s.add(ticketBooked);
                bookedTicketsByuser.put(user.getEmail(), s);
            }
            value.remove(t);
            t.setBookedBy(user);
            if (bookedTicketsBySection.containsKey(entry.getKey())) {
                Set<Ticket> tickets1 = bookedTicketsBySection.get(entry.getKey());
                tickets1.add(t);
            } else {
                Set<Ticket> tickets1 = new HashSet<>();
                tickets1.add(t);
                bookedTicketsBySection.put(entry.getKey(), tickets1);
            }

            break;
        }
        if (ticketBooked != null) {
            return Optional.of(ticketBooked);
        }

        return Optional.empty();
    }

    public Optional<Set<TicketBooked>> getReceipt(String userEmail) {
        if (!bookedTicketsByuser.containsKey(userEmail)) {
            return Optional.empty();
        }
        return Optional.of(bookedTicketsByuser.get(userEmail));
    }

    public Optional<BookedSeatBySection> getBookedTicketBySection(String sectionId) {
        List<SeatInfo> response = null;
        if(bookedTicketsBySection.containsKey(sectionId)) {
            Set<Ticket> tickets = bookedTicketsBySection.get(sectionId);
            Map<User, List<TicketBooked>> ticketsByUser = new HashMap<>();
            for (Ticket t : tickets) {
                if (ticketsByUser.containsKey(t.getBookedBy())) {
                    List<TicketBooked> ticketBookeds = ticketsByUser.get(t.getBookedBy());
                    ticketBookeds.add(new TicketBooked(t.getTicketNo(), t.getSection(), 20));
                } else {
                    List<TicketBooked> ticketBookeds = new ArrayList<>();
                    ticketBookeds.add(new TicketBooked(t.getTicketNo(), t.getSection(), 20));
                    ticketsByUser.put(t.getBookedBy(), ticketBookeds);
                }
            }


            response = ticketsByUser.entrySet().stream()
                    .map(e -> new SeatInfo(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());


        }
        if (response != null && response.isEmpty()) return Optional.empty();

        BookedSeatBySection bookedSeatBySection = new BookedSeatBySection(response);
        return Optional.of(bookedSeatBySection);
    }


    public void deleteUserByMail(User user) {
        if(bookedTicketsByuser.containsKey(user.getEmail())) {
            Set<TicketBooked> ticketBooked = bookedTicketsByuser.get(user.getEmail());
            for (TicketBooked t : ticketBooked) {
                Ticket t1 = new Ticket(t.getTicketNo(), user, t.getSection());
                Set<Ticket> bookedTickets = bookedTicketsBySection.get(t.getSection());
                if(bookedTickets != null) {
                    bookedTickets.remove(t1);
                }
                availableTicketsBySection.get(t.getSection()).add(t1);
            }
            bookedTicketsByuser.remove(user.getEmail());
        }
    }

    public void bookTicketBySeatNo(Ticket ticket, User user) {
        String section = ticket.getSection();
        availableTicketsBySection.get(section).remove(ticket);

        if (bookedTicketsBySection.containsKey(section)) {
            bookedTicketsBySection.get(section).add(ticket);
        } else {
            Set<Ticket> tickets = new HashSet<>();
            tickets.add(ticket);
            bookedTicketsBySection.put(section, tickets);
        }


        if (bookedTicketsByuser.containsKey(user.getEmail())) {
            bookedTicketsByuser.get(user.getEmail()).add(new TicketBooked(ticket.getTicketNo(), ticket.getSection(), 20));
        } else {
            Set<TicketBooked> ticketBooked = new HashSet<>();
            ticketBooked.add(new TicketBooked(ticket.getTicketNo(), ticket.getSection(), 20));
            bookedTicketsByuser.put(user.getEmail(), ticketBooked);
        }
    }

    public boolean hasBooked(User user, TicketInfo booked) {
        if(bookedTicketsByuser.containsKey(user.getEmail())) {
            Set<TicketBooked> ticketBooked = bookedTicketsByuser.get(user.getEmail());
            return ticketBooked.contains(new TicketBooked(booked.getTicketNo(), booked.getSection(), 20));
        }
        return false;
    }

    public boolean isTicketAvailable(Ticket replace) {
        Set<Ticket> tickets1 = availableTicketsBySection.get(replace.getSection());
        return tickets1.contains(replace);
    }

    public Optional<Set<TicketBooked>> replaceTicket(User user, Ticket booked, Ticket replace) {
        booked.setBookedBy(null);
        String section = booked.getSection();
        Set<Ticket> tickets = bookedTicketsBySection.get(section);
        tickets.remove(booked);
        availableTicketsBySection.get(section).add(booked);
        Set<TicketBooked> ticketBookeds = bookedTicketsByuser.get(user.getEmail());
        ticketBookeds.remove(new TicketBooked(booked.getTicketNo() , booked.getSection() , 20));

        replace.setBookedBy(user);
        String section1 = replace.getSection();
        if(bookedTicketsBySection.containsKey(section1)) {
            bookedTicketsBySection.get(section1).add(replace);
        }
        availableTicketsBySection.get(section1).remove(replace);
        if(bookedTicketsByuser.containsKey(user.getEmail())) {
            bookedTicketsByuser.get(user.getEmail()).add(new TicketBooked(replace.getTicketNo() , replace.getSection() , 20));
        }
        else {
            Set<TicketBooked> ticketBooked = bookedTicketsByuser.get(user.getEmail());
            ticketBooked.add(new TicketBooked(replace.getTicketNo() , replace.getSection() , 20));
            bookedTicketsByuser.put(user.getEmail() , ticketBooked);
        }
        return getReceipt(user.getEmail());

    }
}
