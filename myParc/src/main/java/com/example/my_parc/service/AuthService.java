package com.example.my_parc.service;

import com.example.my_parc.domain.User;
import com.example.my_parc.repos.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    private boolean checkPassword(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }

    public User authenticate(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Compare the raw password with the encoded password
            if (checkPassword(password, user.getPassword())) {
                return user;  // Password matches, return user
            } else {
                System.out.println("Password mismatch");
            }
        } else {
            System.out.println("User not found");
        }

        return null;
    }


}