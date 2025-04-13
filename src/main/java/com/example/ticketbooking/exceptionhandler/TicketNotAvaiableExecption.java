package com.example.ticketbooking.exceptionhandler;

public class TicketNotAvaiableExecption extends RuntimeException{

    public TicketNotAvaiableExecption(String message) {
        super(message);
    }
}
