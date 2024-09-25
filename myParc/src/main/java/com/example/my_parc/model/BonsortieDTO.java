package com.example.my_parc.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BonsortieDTO {

    private Long numero;

    @NotNull
    private LocalDate dateSortie;

    @NotNull
    @Size(max = 255)
    private String recepteur;

    @NotNull
    @Size(max = 255)
    private String emetteur;

    @Size(max = 255)
    private String observations;


    private Long userId;

    private List<MaterielDTO> materiels = new ArrayList<>();


}
