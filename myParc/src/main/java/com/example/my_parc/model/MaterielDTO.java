package com.example.my_parc.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MaterielDTO {

    private Long code;

    @NotNull
    private String numeroSerie;

    @NotNull
    @Size(max = 255)
    private String marque;

    @NotNull
    @Size(max = 255)
    private String modele;

    @NotNull
    @Size(max = 255)
    private String libelle;

    @Size(max = 255)
    private String description;

    @Size(max=255)
    private Type typeMateriel;


    private List<@Size(max = 255) String> accesoires;


    private String imageUrl;

    private Status status ;

    private String typeMaterielDisplayName; // New field to store the display name

}
