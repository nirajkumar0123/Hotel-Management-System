package com.hotel.reservation_service.controller;

import com.hotel.reservation_service.dto.ReservationRequest;
import com.hotel.reservation_service.dto.ReservationResponse;
import com.hotel.reservation_service.dto.RoomResponse;
import com.hotel.reservation_service.dto.RoomSearchRequest;
import com.hotel.reservation_service.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/search-rooms")
    public ResponseEntity<List<RoomResponse>> searchRooms(@RequestBody RoomSearchRequest request) {
        return ResponseEntity.ok(reservationService.searchAvailableRooms(request));
    }


    @PostMapping
    public ResponseEntity<ReservationResponse> makeReservation(@RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.makeReservation(request));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @PutMapping("/{roomId}/availability")
    public ResponseEntity<String> updateRoomAvailabilityViaReservation(
            @PathVariable Long roomId, @RequestParam("available") boolean available) {
        reservationService.updateRoomAvailability(roomId, available);
        return ResponseEntity.ok("Room availability updated via Reservation-Service.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.ok("Reservation cancelled successfully.");
    }


}
