package com.hotel.reservation_service.client;

import com.hotel.reservation_service.dto.RoomResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "room-service")
public interface RoomClient {
    @GetMapping("/api/rooms/availability")
    List<RoomResponse> getAvailableRooms(@RequestParam int guests);

    @PutMapping("/api/rooms/{id}/availability")
    void updateRoomAvailability(@PathVariable Long id, @RequestParam("available") boolean available);

}