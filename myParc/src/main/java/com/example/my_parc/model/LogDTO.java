package com.example.my_parc.model;

import com.example.my_parc.domain.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;

public class LogDTO {
    @NotNull @Size(max = 255)
    private Long id;


    @Size(max = 255)
    private User user;
    @Size(max = 255)
    private String action;
    @Size(max = 255)
    private Timestamp date;
}
