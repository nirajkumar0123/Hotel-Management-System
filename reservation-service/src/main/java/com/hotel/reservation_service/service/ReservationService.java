package com.hotel.reservation_service.service;

import com.hotel.reservation_service.client.GuestClient;
import com.hotel.reservation_service.client.RoomClient;
import com.hotel.reservation_service.dto.*;
import com.hotel.reservation_service.entity.Reservation;
import com.hotel.reservation_service.exceptions.RoomNotAvailableException;
import com.hotel.reservation_service.exceptions.RoomNotFoundException;
import com.hotel.reservation_service.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomClient roomClient;
    private final GuestClient guestClient;

    public List<RoomResponse> searchAvailableRooms(RoomSearchRequest request) {
        log.info("Searching available rooms for {} guests between {} and {}",
                request.getNumberOfGuests(), request.getCheckInDate(), request.getCheckOutDate());

        // Fetch all rooms that match capacity from Room-Service
        List<RoomResponse> availableRooms = roomClient.getAvailableRooms(request.getNumberOfGuests());

        // Get all reservations between those dates
        List<Reservation> reservedRooms = reservationRepository.findAll().stream()
                .filter(r -> datesOverlap(r.getCheckInDate(), r.getCheckOutDate(), request.getCheckInDate(), request.getCheckOutDate()))
                .toList();

        Set<Long> reservedRoomIds = reservedRooms.stream()
                .map(Reservation::getRoomId)
                .collect(Collectors.toSet());

        //  Filter out the reserved rooms
        return availableRooms.stream()
                .filter(room -> !reservedRoomIds.contains(room.getId()))
                .collect(Collectors.toList());
    }

    private boolean datesOverlap(LocalDate existingStart, LocalDate existingEnd, LocalDate newStart, LocalDate newEnd) {
        return !(existingEnd.isBefore(newStart) || existingStart.isAfter(newEnd.minusDays(1)));
    }

    public ReservationResponse makeReservation(ReservationRequest request) {
        GuestResponse guest = guestClient.getGuestById(request.getGuestId());
        log.info("Validating guest: {}", guest.getName());

        List<Reservation> conflictingReservations = reservationRepository.findAll().stream()
                .filter(r -> r.getRoomId().equals(request.getRoomId()) &&
                        datesOverlap(r.getCheckInDate(), r.getCheckOutDate(), request.getCheckInDate(), request.getCheckOutDate()))
                .toList();

        if (!conflictingReservations.isEmpty()) {
            throw new RoomNotAvailableException("Room is already reserved for the selected dates.");
        }
        RoomResponse room = roomClient.getAvailableRooms(request.getNumberOfAdults() + request.getNumberOfChildren())
                .stream().filter(r -> r.getId().equals(request.getRoomId()))
                .findFirst().orElseThrow(() -> new RoomNotFoundException("Room not available"));

        Reservation reservation = Reservation.builder()
                .code(request.getCode())
                .numberOfChildren(request.getNumberOfChildren())
                .numberOfAdults(request.getNumberOfAdults())
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .numberOfNights((int) ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate()))
                .status("Confirmed")
                .guestId(guest.getId())
                .roomId(room.getId())
                .build();

        log.info("Reservation made for guest {} in room {}", guest.getName(), room.getRoomNumber());
        ReservationResponse savedReservation = toResponse(reservationRepository.save(reservation));
        roomClient.updateRoomAvailability(request.getRoomId(), false);
        return savedReservation;
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .code(reservation.getCode())
                .numberOfChildren(reservation.getNumberOfChildren())
                .numberOfAdults(reservation.getNumberOfAdults())
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .status(reservation.getStatus())
                .numberOfNights(reservation.getNumberOfNights())
                .guestId(reservation.getGuestId())
                .roomId(reservation.getRoomId())
                .build();
    }

    public void updateRoomAvailability(Long roomId, boolean available) {
        log.info("Updating availability of room {} to {}", roomId, available);
        roomClient.updateRoomAvailability(roomId, available);
    }

    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // Call Room-Service to mark room available
        roomClient.updateRoomAvailability(reservation.getRoomId(), true);

        log.info("Cancelling reservation {} for room {}", reservation.getId(), reservation.getRoomId());
        reservationRepository.deleteById(id);
    }
}
