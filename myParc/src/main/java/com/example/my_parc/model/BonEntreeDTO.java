package com.example.my_parc.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BonEntreeDTO {

    private Long numero;

    @NotNull
    private LocalDate dateEntree;

    @NotNull
    @Size(max = 255)
    private String recepteur;

    @NotNull
    @Size(max = 255)
    private String emetteur;

    @Size(max = 255)
    private String observations;

    private List<@Size(max = 255) String> listeArticles;

    private Long userId;

    private List<MaterielDTO> materiels ;

}
