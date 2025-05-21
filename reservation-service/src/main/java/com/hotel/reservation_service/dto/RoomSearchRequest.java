package com.hotel.reservation_service.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RoomSearchRequest {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
}
