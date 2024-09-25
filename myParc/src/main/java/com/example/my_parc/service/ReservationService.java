package com.example.my_parc.service;

import com.example.my_parc.domain.Materiel;
import com.example.my_parc.domain.Reservation;
import com.example.my_parc.domain.User;
import com.example.my_parc.model.ReservationDTO;
import com.example.my_parc.model.Status;
import com.example.my_parc.repos.MaterielRepository;
import com.example.my_parc.repos.ReservationRepository;
import com.example.my_parc.repos.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MaterielRepository materielRepository;

    @Transactional
    public Reservation createReservation(ReservationDTO reservationDTO) {
        User user = userRepository.findById(reservationDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Materiel materiel = materielRepository.findById(reservationDTO.getMaterielId())
                .orElseThrow(() -> new RuntimeException("Materiel not found"));

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setMateriel(materiel);
        reservation.setStatus(Reservation.ReservationStatus.EN_COURS);
        reservation.setDateOfReservation(LocalDate.now()); // Set the current date for reservation

        return reservationRepository.save(reservation);
    }

    // Method to fetch reservations by user ID
    public List<Reservation> findByUserId(Long userId) {
        return reservationRepository.findAllByUserId(userId);
    }

    public Reservation updateReservationStatus(Long reservationId, Reservation.ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }

    public  List <Reservation> findAll(){
        return reservationRepository.findAll();
    }

    public Map<Materiel, Long> getMostDemandedMaterials() {
        List<Object[]> result = reservationRepository.findMostDemandedMaterials();

        Map<Materiel, Long> mostDemandedMaterials = new HashMap<>();
        for (Object[] row : result) {
            Materiel materiel = (Materiel) row[0];
            Long demandCount = (Long) row[1];
            mostDemandedMaterials.put(materiel, demandCount);
        }

        return mostDemandedMaterials;
    }

    public Map<Integer, Long> getReservationsByDayInMonth(int month, int year) {
        List<Reservation> reservations = reservationRepository.findAll(); // Or use a more efficient query
        Map<Integer, Long> reservationsByDay = new HashMap<>();

        reservations.stream()
                .filter(reservation -> reservation.getDateOfReservation().getMonthValue() == month
                        && reservation.getDateOfReservation().getYear() == year)
                .forEach(reservation -> {
                    int day = reservation.getDateOfReservation().getDayOfMonth();
                    reservationsByDay.put(day, reservationsByDay.getOrDefault(day, 0L) + 1);
                });

        return reservationsByDay;
    }


    public Long CountReservationsByStatus(Reservation.ReservationStatus status){
        return reservationRepository.countReservationsByStatus(status);
    }







}
