package com.example.my_parc.model;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotNull
    private Role role;

    @NotNull
    @Size(max = 255)
    private String nom;

    @NotNull
    @Size(max = 255)
    private String prenom;

    @NotNull
    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    @Transient
    @Size(max = 255)
    private String confirmPassword;


}
