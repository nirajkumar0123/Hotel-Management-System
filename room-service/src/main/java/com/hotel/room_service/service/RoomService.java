package com.hotel.room_service.service;

import com.hotel.room_service.dto.*;
import com.hotel.room_service.entity.Room;
import com.hotel.room_service.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomResponse addRoom(RoomRequest request) {
        if (roomRepository.findByRoomNumber(request.getRoomNumber()).isPresent()) {
            throw new IllegalArgumentException("Room number already exists");
        }
        Room room = Room.builder()
                .roomNumber(request.getRoomNumber())
                .roomType(request.getRoomType())
                .capacity(request.getCapacity())
                .pricePerNight(request.getPricePerNight())
                .isAvailable(request.isAvailable())
                .build();
        return toResponse(roomRepository.save(room));
    }

    public List<RoomResponse> getAvailableRooms(int guests) {
        return roomRepository.findByCapacityGreaterThanEqualAndIsAvailableTrue(guests)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public RoomResponse updateRoom(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(request.getRoomType());
        room.setCapacity(request.getCapacity());
        room.setPricePerNight(request.getPricePerNight());
        room.setAvailable(request.isAvailable());
        return toResponse(roomRepository.save(room));
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public RoomResponse getRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return toResponse(room);
    }

    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public RoomResponse updateRoomAvailability(Long id, boolean available) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setAvailable(available);
        log.info("Updating availability of room {} to {}", room.getRoomNumber(), available);
        return toResponse(roomRepository.save(room));
    }

    private RoomResponse toResponse(Room room) {
        return new RoomResponse(
                room.getId(), room.getRoomNumber(), room.getRoomType(),
                room.getCapacity(), room.getPricePerNight(), room.isAvailable());
    }
}
