package com.hotel.reservation_service.dto;

import lombok.Data;

@Data
public class GuestRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
}
