package com.example.ticketbooking.exceptionhandler;

public class InvalidTicketDetailsException extends RuntimeException {

    public InvalidTicketDetailsException(String message) {
        super(message);
    }
}
