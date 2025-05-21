package com.hotel.guest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestResponse {
    private Long id;
    private String memberCode;
    private String phoneNumber;
    private String company;
    private String name;
    private String email;
    private String gender;
    private String address;

}
