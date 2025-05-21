package com.hotel_management.authentication_service.service;

import com.hotel_management.authentication_service.dto.*;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    String signup(SignupRequest request);

    boolean ownerExists();
}
