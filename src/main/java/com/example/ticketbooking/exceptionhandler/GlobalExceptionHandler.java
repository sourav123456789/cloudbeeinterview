package com.example.ticketbooking.exceptionhandler;

import com.example.ticketbooking.dto.ApiResponse;
import com.example.ticketbooking.dto.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TicketNotAvaiableExecption.class)
    public ResponseEntity<Object> TicketNotAvailable(TicketNotAvaiableExecption ex,
                                                     WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorMessage(ex.getLocalizedMessage());
        ApiResponse apiResponse = new ApiResponse(response, null);
        return this.handleExceptionInternal(ex, apiResponse, null, HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        ApiResponse apiResponse = new ApiResponse(errors, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnCachedException(Exception ex,
                                                          WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorMessage(ex.getLocalizedMessage());
        ApiResponse apiResponse = new ApiResponse(response, null);
        return this.handleExceptionInternal(ex, apiResponse, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


}
