package com.hotel.room_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {
    private String roomNumber;
    private String roomType;
    private int capacity;
    private double pricePerNight;
    private boolean available;
}
