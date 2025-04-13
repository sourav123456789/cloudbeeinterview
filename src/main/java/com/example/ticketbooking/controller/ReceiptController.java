package com.example.ticketbooking.controller;

import com.example.ticketbooking.dto.ApiResponse;
import com.example.ticketbooking.dto.TicketBooked;
import com.example.ticketbooking.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/receipt")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping("/{user}")
    public ResponseEntity<Object> getReceipt(@PathVariable("user") String userName) {
        Set<TicketBooked> receipt = receiptService.getReceipt(userName);
        ApiResponse apiResponse = new ApiResponse(null , receipt);
        return new ResponseEntity<>(apiResponse , HttpStatus.OK);
    }
}
