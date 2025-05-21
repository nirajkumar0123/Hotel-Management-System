package com.hotel_management.authentication_service.service;

import com.hotel_management.authentication_service.dto.*;
import com.hotel_management.authentication_service.entity.Staff;
import com.hotel_management.authentication_service.enums.Role;
import com.hotel_management.authentication_service.repository.StaffRepository;
import com.hotel_management.authentication_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Staff user = staffRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return new AuthResponse(token);
    }

    @Override
    public boolean ownerExists() {
        return staffRepository.findAll().stream()
                .anyMatch(user -> user.getRole() == Role.OWNER);
    }


    @Override
    public String signup(SignupRequest request) {
        Staff user = Staff.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.RECEPTIONIST)
                .build();
        staffRepository.save(user);
        return "User registered successfully!";
    }
}
