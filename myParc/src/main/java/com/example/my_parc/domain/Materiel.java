package com.example.my_parc.domain;

import com.example.my_parc.model.Status;
import com.example.my_parc.model.Type;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Entity
@Table(name = "Materiels")
@Getter
@Setter
public class Materiel {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @Column(nullable = false)
    private String numeroSerie;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type typeMateriel;

    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private String modele;

    @Column(nullable = false)
    private String libelle;

    @Column(name = "\"description\"")
    private String description;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> accesoires;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "materiels")
    private Set<BonEntree> bonEntrees = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "materiels")
    private Set<Bonsortie> bonSorties = new HashSet<>();




    @Column(nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)

    @Column(nullable = false)
    private Status status ;


}
