package com.example.my_parc.repos;

import com.example.my_parc.domain.Reservation;
import com.example.my_parc.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUserId(Long userId);

    // Query to count how many times each material was reserved
    @Query("SELECT r.materiel, COUNT(r.materiel) as demandCount " +
            "FROM Reservation r " +
            "GROUP BY r.materiel " +
            "ORDER BY demandCount DESC")
    List<Object[]> findMostDemandedMaterials();


    Long countByStatus(Status status);

    Long countReservationsByStatus(Reservation.ReservationStatus status);
}
