package com.example.my_parc.repos;

import com.example.my_parc.domain.BonEntree;
import com.example.my_parc.domain.Bonsortie;
import com.example.my_parc.domain.Materiel;
import com.example.my_parc.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MaterielRepository extends JpaRepository<Materiel, Long> {
    Materiel findFirstByBonEntreesContains(BonEntree bonEntree);
    Materiel findFirstByBonSortiesContains(Bonsortie bonSortie);

    List<Materiel> findByStatus(Status status);

    long countByStatus(Status status);
}


