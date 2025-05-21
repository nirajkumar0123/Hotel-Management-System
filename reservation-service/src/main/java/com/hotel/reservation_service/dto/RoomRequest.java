package com.hotel.reservation_service.dto;

import lombok.Data;

@Data
public class RoomRequest {
    private String roomNumber;
    private String roomType;
    private int capacity;
    private double pricePerNight;
    private boolean available;
}
