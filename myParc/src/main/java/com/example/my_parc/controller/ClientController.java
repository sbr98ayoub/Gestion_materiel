package com.example.my_parc.controller;

import com.example.my_parc.domain.Materiel;
import com.example.my_parc.domain.Reservation;
import com.example.my_parc.domain.User;
import com.example.my_parc.model.BonsortieDTO;
import com.example.my_parc.model.MaterielDTO;
import com.example.my_parc.model.ReservationDTO;
import com.example.my_parc.model.Status;
import com.example.my_parc.service.BonsortieService;
import com.example.my_parc.service.LogService;
import com.example.my_parc.service.MaterielService;
import com.example.my_parc.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/client")
public class ClientController {

    private final MaterielService materielService;
    private final ReservationService reservationService;

    private  final BonsortieService bonsortieService;
    private  final LogService logService ;

    public ClientController(MaterielService materielService, ReservationService reservationService, BonsortieService bonsortieService, LogService logService) {
        this.materielService = materielService;
        this.reservationService = reservationService;
        this.bonsortieService = bonsortieService;
        this.logService = logService;
    }

    @GetMapping
    public String getClients(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            System.out.println("user is null");
            return "redirect:/"; // Redirect to login if the user is not logged in
        }

        // Add user details to model
        model.addAttribute("user", loggedInUser);

        // Fetch available materiel
        List<MaterielDTO> materielList = materielService.findByStatus(Status.IN_STOCK);
        model.addAttribute("materielList", materielList);

        return "home/client";
    }

    @GetMapping("/reserve")
    public String reserveMateriel(@RequestParam Long materielId, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/"; // Redirect to login if not logged in
        }

        // Check if the user has already reserved this material
        List<Reservation> existingReservations = reservationService.findByUserId(loggedInUser.getId());
        boolean alreadyReserved = existingReservations.stream()
                .anyMatch(reservation -> reservation.getMateriel().getCode().equals(materielId));

        if (alreadyReserved) {
            // Add an error message to the model and redirect
            redirectAttributes.addFlashAttribute("reservationError", "You have already reserved this material.");
            return "redirect:/client"; // Redirect back to client page
        }

        // Check if materiel is still available
        MaterielDTO materiel = materielService.get(materielId);
        if (materiel == null || !materiel.getStatus().equals(Status.IN_STOCK)) {
            redirectAttributes.addFlashAttribute("reservationError", "Material is unavailable or already reserved.");
            return "redirect:/client"; // Redirect back to client page
        }

        // Create a ReservationDTO and populate it
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setUserId(loggedInUser.getId());
        reservationDTO.setMaterielId(materielId);
        reservationDTO.setDateOfReservation(LocalDate.now());
        reservationDTO.setMaterielId(materiel.getCode());

        // Create reservation
        reservationService.createReservation(reservationDTO);

        // Add a success message to the model and redirect
        redirectAttributes.addFlashAttribute("reservationSuccess", "Material reserved successfully.");
        logService.createLog(loggedInUser,"reserves a materiel :" + materiel.getMarque()+" "+materiel.getModele());

        return "redirect:/client";
    }


    @GetMapping("/reservations")
    public String getUserReservations(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/";
        }

        // Get reservations for the logged-in user
        List<Reservation> reservations = reservationService.findByUserId(loggedInUser.getId());
        if (reservations.isEmpty()) {
            model.addAttribute("message", "You have no reservations.");
        }
        model.addAttribute("reservationList", reservations);
        model.addAttribute("user", loggedInUser);

        return "home/reservation";
    }

}
