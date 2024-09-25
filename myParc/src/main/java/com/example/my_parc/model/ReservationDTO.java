package com.example.my_parc.model;

import com.example.my_parc.domain.Reservation.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationDTO {
    private Long id;
    private Long userId;
    private Long materielId;
    private ReservationStatus status;
    private LocalDate dateOfReservation;
}
