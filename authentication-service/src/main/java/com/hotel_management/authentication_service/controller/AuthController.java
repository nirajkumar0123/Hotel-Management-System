package com.hotel_management.authentication_service.controller;

import com.hotel_management.authentication_service.dto.*;
import com.hotel_management.authentication_service.enums.Role;
import com.hotel_management.authentication_service.service.AuthService;
import com.hotel_management.authentication_service.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/init-owner")
    public ResponseEntity<String> initOwner(@RequestBody SignupRequest request) {
        if (request.getRole() != Role.OWNER) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This route only creates OWNER role.");
        }

        // Check if any OWNER already exists
        boolean ownerExists = authService.ownerExists();
        if (ownerExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("An OWNER already exists.");
        }

        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody SignupRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        // Extract role from the token
        String token = authHeader.substring(7);
        Claims claims = jwtUtil.extractAllClaims(token);
        String creatorRole = claims.get("role", String.class);

        // Enforce rules
        if (request.getRole() == Role.OWNER && !creatorRole.equals("OWNER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only OWNER can create other OWNERS");
        }

        if (request.getRole() == Role.MANAGER && !creatorRole.equals("OWNER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only OWNER can create MANAGER");
        }

        if (request.getRole() == Role.RECEPTIONIST && !(creatorRole.equals("OWNER") || creatorRole.equals("MANAGER"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only OWNER or MANAGER can create RECEPTIONIST");
        }

        return ResponseEntity.ok(authService.signup(request));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
