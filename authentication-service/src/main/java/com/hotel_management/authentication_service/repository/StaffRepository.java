package com.hotel_management.authentication_service.repository;

import com.hotel_management.authentication_service.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByUsername(String username);
}

