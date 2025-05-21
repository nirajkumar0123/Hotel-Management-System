package com.hotel.reservation_service.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            RoomNotAvailableException.class,
            GuestNotFoundException.class,
            RoomNotFoundException.class
    })
    public ResponseEntity<String> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
