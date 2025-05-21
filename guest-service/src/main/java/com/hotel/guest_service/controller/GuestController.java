package com.hotel.guest_service.controller;

import com.hotel.guest_service.dto.GuestRequest;
import com.hotel.guest_service.dto.GuestResponse;
import com.hotel.guest_service.service.GuestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @PostMapping
    public ResponseEntity<GuestResponse> add(@RequestBody GuestRequest guestRequest) {
        return ResponseEntity.ok(guestService.addGuest(guestRequest));
    }

    @GetMapping
    public ResponseEntity<List<GuestResponse>> getAll() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(guestService.getGuestById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        guestService.deleteGuest(id);
        return ResponseEntity.noContent().build();
    }
}
