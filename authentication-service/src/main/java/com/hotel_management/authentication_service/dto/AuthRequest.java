package com.hotel_management.authentication_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
}

