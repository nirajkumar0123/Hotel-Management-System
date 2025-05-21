package com.hotel.guest_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestRequest {

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String company;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Address is required")
    private String address;
}
