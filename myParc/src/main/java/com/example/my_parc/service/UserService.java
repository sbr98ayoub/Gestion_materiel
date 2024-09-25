package com.example.my_parc.service;

import com.example.my_parc.domain.User;
import com.example.my_parc.model.Role;
import com.example.my_parc.model.UserDTO;
import com.example.my_parc.repos.UserRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Helper method to encode password
    private String encodePassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO get(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    }

    public void create(UserDTO userDTO) {
        User user = new User();
        user.setNom(userDTO.getNom());
        user.setPrenom(userDTO.getPrenom());
        user.setEmail(userDTO.getEmail());

        // Encode password before saving
        user.setPassword(encodePassword(userDTO.getPassword()));
        user.setRole(userDTO.getRole());

        userRepository.save(user);
    }

    public void update(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        user.setNom(userDTO.getNom());
        user.setPrenom(userDTO.getPrenom());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());

        // If the password is provided, encode it before saving
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(encodePassword(userDTO.getPassword()));
        }

        userRepository.save(user);
    }

    public void delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Invalid user Id:" + id);
        }
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setNom(user.getNom());
        userDTO.setPrenom(user.getPrenom());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public void importUsers(MultipartFile file) throws IOException, CsvValidationException {
        List<UserDTO> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csvReader = new CSVReader(reader)) {

            String[] nextRecord;
            boolean isHeader = true;

            while ((nextRecord = csvReader.readNext()) != null) {
                if (isHeader) {
                    // Skip the first row (header)
                    isHeader = false;
                    continue;
                }

                // Assuming columns are Nom, Prenom, Email, and Role (in that order)
                UserDTO user = new UserDTO();
                user.setNom(nextRecord[0]);   // Nom
                user.setPrenom(nextRecord[1]); // Prenom
                user.setEmail(nextRecord[2]);  // Email
                user.setRole(Role.valueOf(nextRecord[3].toUpperCase()));
                user.setPassword(nextRecord[4]);

                users.add(user);
            }
        }

        // Save all users to the database
        for (UserDTO user : users) {
            this.create(user);
        }


    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}