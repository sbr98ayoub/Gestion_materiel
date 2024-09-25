package com.example.my_parc.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Entity
@Table(name = "Bonsorties")
@Getter
@Setter
public class Bonsortie {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numero;

    @Column(nullable = false)
    private LocalDate dateSortie;

    @Column(nullable = false)
    private String recepteur;

    @Column(nullable = false)
    private String emetteur;

    @Column
    private String observations;


    @ManyToMany
    @JoinTable(
            name = "BonSortie_Materiel",
            joinColumns = @JoinColumn(name = "bonSortie_id"),
            inverseJoinColumns = @JoinColumn(name = "materiel_id"))
    private Set<Materiel> materiels = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_id")
    private User userId;

}
