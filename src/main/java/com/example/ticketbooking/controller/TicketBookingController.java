package com.example.ticketbooking.controller;

import com.example.ticketbooking.dto.*;
import com.example.ticketbooking.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketBookingController {

    private final TicketService ticketService;

    // This endpoint is for booking ticket
    @PostMapping("/")
    public ResponseEntity<Object> bookTicket(@Valid @RequestBody TicketRequest ticketRequest) {
        Receipt ticketResponse = ticketService.bookTicket(ticketRequest);
        ApiResponse apiResponse = new ApiResponse(null , ticketResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/ticketBySection/{sectionId}")
    public ResponseEntity<Object> getBookedTicketDetailsBySection(@PathVariable("sectionId")
                                                                  String sectionId) {
        BookedSeatBySection bookedTicketBySectionId = ticketService.getBookedTicketBySectionId(sectionId);
        ApiResponse apiResponse = new ApiResponse(null , bookedTicketBySectionId);
        return new ResponseEntity<>(apiResponse , HttpStatus.OK);
    }

    //An API to remove a user from the train
    @DeleteMapping("/deletebyuser")
    public ResponseEntity<Object> removeUser(
            @RequestParam("userMail") String userMail,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName ){
        User user = new User(firstName , lastName , userMail);
        ticketService.deleteTicketForUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //An API to modify a user's seat
    /* This api will cancel all the user existing ticket and book the
     new tickets passed in the request body*/
    @PutMapping("/")
    public ResponseEntity<Object> updateSeat(@Valid @RequestBody UpdateSeatRequestResponse updateSeat){
        ticketService.deleteTicketForUser(updateSeat.getUser());
        UpdateSeatRequestResponse updateSeatRequestResponse = ticketService.bookSpecificTicket(updateSeat);
        ApiResponse apiResponse = new ApiResponse(null , updateSeatRequestResponse);
        return new ResponseEntity<>(apiResponse , HttpStatus.OK);
    }

    /* Api to replace an existing ticket */

    @PutMapping("/replaceTicket")
    public ResponseEntity<Object> replaceTicket(@Valid @RequestBody ReplaceTicketRequest replaceTicketRequest) {
        Set<TicketBooked> receipt = ticketService.replaceTicket(replaceTicketRequest);
        ApiResponse apiResponse = new ApiResponse(null , receipt);
        return new ResponseEntity<>(apiResponse , HttpStatus.OK);
    }








}
