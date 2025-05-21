package com.hotel.room_service.controller;

import com.hotel.room_service.dto.*;
import com.hotel.room_service.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponse> addRoom(@RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.addRoom(request));
    }

    @GetMapping("/availability")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(@RequestParam int guests) {
        return ResponseEntity.ok(roomService.getAvailableRooms(guests));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id, @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoom(id));
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<RoomResponse> updateAvailability(@PathVariable Long id, @RequestParam("available") boolean available) {
        return ResponseEntity.ok(roomService.updateRoomAvailability(id, available));
    }

}
