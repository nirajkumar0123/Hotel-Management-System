package com.hotel.reservation_service.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservationRequest {
    private String code;
    private int numberOfChildren;
    private int numberOfAdults;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long guestId;
    private Long roomId;
}
