package com.example.my_parc.controller;

import com.example.my_parc.domain.User;
import com.example.my_parc.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class HomeController {
    @Autowired
    private AuthService authService;
    @GetMapping("/")
    public String index() {
        return "home/index";
    }



    @GetMapping("/user")
    public String user(){return "home/user";}


    @PostMapping("/")
    public String login(@RequestParam String email, @RequestParam String password, @RequestParam String role, HttpSession session, Model model) {
        try {
            User user = authService.authenticate(email, password);
            if (user != null) {
                session.setAttribute("loggedInUser", user);
                // Compare the selected role and the user's actual role from the database
                if (!user.getRole().toString().equalsIgnoreCase(role)) {
                    model.addAttribute("error", "Selected role does not match the user's actual role.");
                    return "home/index";  // Redirect back to login page with error
                }

                // Redirect based on the user's actual role
                if ("ADMIN".equals(user.getRole().toString())) {
                    model.addAttribute("success", "Successfully logged in as Admin.");
                    return "redirect:/admin";
                } else if ("CLIENT".equals(user.getRole().toString())) {
                    model.addAttribute("success", "Successfully logged in as Client.");
                    return "redirect:/client";
                } else if ("RESPONSABLE_STOCK".equals(user.getRole().toString())) {
                    session.setAttribute("responsable", user);
                    model.addAttribute("success", "Successfully logged in as Responsable Stock.");
                    return "redirect:/responsable";
                } else {
                    model.addAttribute("error", "Unrecognized role.");
                    return "home/index";
                }
            } else {
                model.addAttribute("error", "Invalid email or password.");
                return "home/index";
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred. Please try again.");
            return "home/index";
        }
    }



    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();  // Invalidate the session
        redirectAttributes.addFlashAttribute("logoutSuccess", "You have been successfully logged out.");
        return "redirect:/";
    }


}


