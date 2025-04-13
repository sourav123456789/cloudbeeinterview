package com.example.ticketbooking.repositry;

import com.example.ticketbooking.dto.*;
import com.example.ticketbooking.entity.Ticket;
import com.example.ticketbooking.exceptionhandler.TicketNotAvaiableExecption;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TicketRepository {

    private static final Map<String, PriorityQueue<Ticket>> tickets = new HashMap<>();

    private Map<String, Set<TicketBooked>> bookedTickets = new HashMap<>();

    static {
        PriorityQueue<Ticket> t1 = new PriorityQueue<>((ticket1, ticket2) -> {
            if (ticket1.getBookedBy() == null && ticket2.getBookedBy() != null) {
                return -1;
            } else if (ticket1.getBookedBy() != null && ticket2.getBookedBy() == null) {
                return 1;
            }
            return ticket1.getTicketNo().compareTo(ticket2.getTicketNo());
        });

        PriorityQueue<Ticket> t2 = new PriorityQueue<>((ticket1, ticket2) -> {
            if (ticket1.getBookedBy() == null && ticket2.getBookedBy() != null) {
                return -1;
            } else if (ticket1.getBookedBy() != null && ticket2.getBookedBy() == null) {
                return 1;
            }
            return ticket1.getTicketNo().compareTo(ticket2.getTicketNo());
        });

        for (int i = 1; i <= 1000; i++) {
            t1.add(new Ticket("A" + i, null, "A"));
            t2.add(new Ticket("B" + i, null, "B"));
        }
        tickets.put("A", t1);
        tickets.put("B", t2);
    }


    // This method will book a ticket from one of the section based on availability
    public Optional<TicketBooked> bookTicket(User user) {
        Ticket ticket = null;
        TicketBooked ticketBooked = null;
        for (Map.Entry<String, PriorityQueue<Ticket>> entry : tickets.entrySet()) {
            PriorityQueue<Ticket> value = entry.getValue();
            if (value.isEmpty()) continue;
            Ticket t = value.poll();
            if (t.getBookedBy() == null) {
                ticketBooked = new TicketBooked();
                ticketBooked.setTicketNo(t.getTicketNo());
                ticketBooked.setSection(entry.getKey());
                ticketBooked.setPrice(20);
                //update the bookedBy value and put it inside priorityQueue to rebalance
                t.setBookedBy(user);
                value.add(t);
                // updating the bookedTickets map
                if (bookedTickets.containsKey(user.getEmail())) {
                    Set<TicketBooked> ticketBook = bookedTickets.get(user.getEmail());
                    ticketBook.add(ticketBooked);
                } else {
                    Set<TicketBooked> s = new HashSet<>();
                    s.add(ticketBooked);
                    bookedTickets.put(user.getEmail(), s);
                }
                break;
            }

        }
        if (ticketBooked != null) {
            return Optional.of(ticketBooked);
        }

        return Optional.empty();
    }

    public Optional<Set<TicketBooked>> getReceipt(String userEmail) {
        if (!bookedTickets.containsKey(userEmail)) {
            return Optional.empty();
        }
        return Optional.of(bookedTickets.get(userEmail));
    }

    public Optional<BookedSeatBySection> getBookedTicketBySection(String sectionId) {
        Map<User, List<TicketBooked>> userToTickets = new HashMap<>();

        // copy the priorityQueue to avoid changing the original one
        PriorityQueue<Ticket> pq = new PriorityQueue<>(tickets.get(sectionId));
        while (!pq.isEmpty()) {
            Ticket ticket = pq.poll();
            if (ticket.getBookedBy() != null) {
                userToTickets
                        .computeIfAbsent(ticket.getBookedBy(), u -> new ArrayList<>())
                        .add(new TicketBooked(ticket.getTicketNo(), sectionId, 20));
            }
        }


        List<SeatInfo> response = userToTickets.entrySet().stream()
                .map(e -> new SeatInfo(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        if (response.isEmpty()) return Optional.empty();

        BookedSeatBySection bookedSeatBySection = new BookedSeatBySection(response);
        return Optional.of(bookedSeatBySection);
    }


    public void deleteUserByMail(String userMail) {
        Set<TicketBooked> ticketBookedByUser = bookedTickets.get(userMail);
        if (ticketBookedByUser == null || ticketBookedByUser.isEmpty()) return;
        for (TicketBooked ticket : ticketBookedByUser) {
            String section = ticket.getSection();
            PriorityQueue<Ticket> tickets1 = tickets.get(section);
            for (Ticket t : tickets1) {
                if (t.getTicketNo().equals(ticket.getTicketNo())
                        && t.getBookedBy().getEmail().equals(userMail)) {
                    t.setBookedBy(null);
                }
            }

        }
        bookedTickets.remove(userMail);
    }

    public UpdateSeatRequestResponse bookTicketBySeatNo(UpdateSeatRequestResponse updateSeat) {
        PriorityQueue<Ticket> available = tickets.get(updateSeat.getInfo().getSection());
        boolean ticketAvailable = false;
        for (Ticket t : available) {
            if (t.getBookedBy() == null && t.getTicketNo().equals(updateSeat.getInfo().getTicketNo())) {
                ticketAvailable = true;
                Set<TicketBooked> ticketBookeds = new HashSet<>();
                ticketBookeds.add(new TicketBooked(
                        updateSeat.getInfo().getTicketNo(),
                        updateSeat.getInfo().getSection(),
                        20
                ));
                t.setBookedBy(updateSeat.getUser());
                bookedTickets.put(updateSeat.getUser().getEmail(), ticketBookeds);
                break;
            }
        }
        if (!ticketAvailable) {
            throw new TicketNotAvaiableExecption("Ticket not available");
        }
        return updateSeat;
    }
}
