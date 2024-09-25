package com.example.my_parc.controller;

import com.example.my_parc.domain.Reservation;
import com.example.my_parc.domain.User;
import com.example.my_parc.model.*;
import com.example.my_parc.service.*;
import com.example.my_parc.util.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/responsable")
public class ResponsableController {

    private final MaterielService materielService;
    private final BonEntreeService bonEntreeService;
    private final BonsortieService bonSortieService;
    private final ReservationService reservationService ;
    private final LogService logService ;


    @Autowired
    public ResponsableController(MaterielService materielService, BonEntreeService bonEntreeService, BonsortieService bonsortieService, ReservationService reservationService, LogService logService) {
        this.materielService = materielService;
        this.bonEntreeService = bonEntreeService;
        this.bonSortieService=bonsortieService;
        this.reservationService = reservationService;
        this.logService = logService;
    }

    @GetMapping
    public String showDashboard(HttpSession session, Model model) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Long numeberOfUnprocessedReservations = reservationService.CountReservationsByStatus(Reservation.ReservationStatus.EN_COURS);
        Long materielInStock = materielService.countByStatus(Status.IN_STOCK);
        Long materielOutOfStock = materielService.countByStatus(Status.OUT_OF_STOCK);

        // Fetch material type distribution for the second chart
        Map<String, Long> materielTypeCounts = materielService.countMaterielsByType();


        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String materielTypeCountsJson = objectMapper.writeValueAsString(materielTypeCounts);
            model.addAttribute("materielTypeCountsJson", materielTypeCountsJson);        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loggedInUser != null) {
            model.addAttribute("user1",loggedInUser);
            session.setAttribute("user3",loggedInUser);
            model.addAttribute("numberofReservations",numeberOfUnprocessedReservations);
            model.addAttribute("materielInStock",materielInStock);
            model.addAttribute("materielOutOfStock",materielOutOfStock);
            model.addAttribute("materielTypeCounts", materielTypeCounts);
            System.out.println("In Stock: " + materielInStock);
            System.out.println("Out of Stock: " + materielOutOfStock);
            System.out.println("Type Counts: " + materielTypeCounts);


            System.out.println("the user is not null and here is the prenom"+loggedInUser.getPrenom());// Pass the user object to the template
            return "home/responsable";
        } else {
            System.out.println("the user is null there is no responsible.");
            return "redirect:/";
        }
    }



    @GetMapping("/materiels")
    public String showMaterielList(HttpSession session,Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        List<MaterielDTO> materielList = materielService.findAll();
        model.addAttribute("materielList", materielList);
        model.addAttribute("loggedInUser", user);

        return "home/materiel";
    }

    @GetMapping("/materiel/add")
    public String showAddMaterielForm(Model model) {
        model.addAttribute("materielDTO", new MaterielDTO());
        List<Type>  enums=Arrays.asList(Type.values());
        model.addAttribute("typeMaterielList",enums); //
        System.out.println(enums);

        return "home/materiel";
    }

    @PostMapping("/materiel/add")
    public String addMateriel(
            @ModelAttribute MaterielDTO materielDTO,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes,HttpSession session) {

            User loggedInUser = (User) session.getAttribute("loggedInUser");

        try {
            materielService.create(materielDTO, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Matériel ajouté avec succès !");
            logService.createLog(loggedInUser,"adding a new materiel :" + materielDTO.getMarque() +" "+ materielDTO.getModele());
            return "redirect:/responsable/materiels";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'ajout du matériel.");
            return "redirect:/responsable/materiels";
        }
    }

    @GetMapping("/materiel/edit/{id}")
    public String showEditMaterielForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        try {
            MaterielDTO materielDTO = materielService.get(id);
            model.addAttribute("materielDTO", materielDTO);
            return "home/materiel";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la récupération du matériel.");
            return "redirect:/responsable/materiels";
        }
    }

    @PostMapping("/materiel/edit/{id}")
    public String editMateriel(
            @PathVariable Long id,
            @ModelAttribute MaterielDTO materielDTO,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes,HttpSession session) {

            User loggedInUser = (User) session.getAttribute("loggedInUser");

        try {
            materielService.update(id, materielDTO, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Matériel mis à jour avec succès !");
            logService.createLog(loggedInUser,"editing the materiel with id : " +id);
            return "redirect:/responsable/materiels";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la mise à jour du matériel.");
            return "redirect:/responsable/materiels";
        }
    }

    @PostMapping("/materiel/delete/{id}")
    public String deleteMateriel(@PathVariable Long id, RedirectAttributes redirectAttributes,HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        try {
            materielService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Matériel supprimé avec succès !");
            logService.createLog(loggedInUser,"deleting materiel with the id : "+ id);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression du matériel.");
        }
        return "redirect:/responsable/materiels";
    }

    @GetMapping("/bonEntree")
    public String showBonEntreePage(Model model) {
        // Fetch and display Bon d'Entrée records
        List<BonEntreeDTO> bonEntreeList = bonEntreeService.findAll();

        // Fetch materials with status OUT_OF_STOCK
        List<MaterielDTO> materielList = materielService.findByStatus(Status.OUT_OF_STOCK);
        System.out.println("Materiels OUT_OF_STOCK: " + materielList.size());



        model.addAttribute("bonEntreeList", bonEntreeList);
        model.addAttribute("materielList", materielList);
        model.addAttribute("bonEntreeDTO", new BonEntreeDTO());

        return "home/bonEntree";
    }


    @PostMapping("/bonEntree/add")
    public String addBonEntree(
            @ModelAttribute BonEntreeDTO bonEntreeDTO,
            @RequestParam("materielIds") List<Long> materielIds,
            RedirectAttributes redirectAttributes,HttpSession session) {

         User loggedInUser = (User) session.getAttribute("loggedInUser");


        try {
            bonEntreeService.create(bonEntreeDTO, materielIds);
            redirectAttributes.addFlashAttribute("successMessage", "Bon d'Entrée ajouté avec succès !");
            logService.createLog(loggedInUser,"creating a new stock entry with the id :  "+bonEntreeDTO.getNumero());
            return "redirect:/responsable/bonEntree";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'ajout du Bon d'Entrée.");
            return "redirect:/responsable/bonEntree";
        }
    }
    @GetMapping("/bonSortie")
    public String showBonSortiePage(Model model) {
        // Fetch and display Bon de Sortie records
        List<BonsortieDTO> bonSortieList = bonSortieService.findAll();

        // Fetch materials with status IN_STOCK
        List<MaterielDTO> materielList = materielService.findByStatus(Status.IN_STOCK);
        System.out.println("Materiels IN_STOCK: " + materielList.size());

        model.addAttribute("bonSortieList", bonSortieList);
        model.addAttribute("materielList", materielList);
        model.addAttribute("bonSortieDTO", new BonsortieDTO());

        return "home/bonSortie";
    }

    @PostMapping("/bonSortie/add")
    public String addBonSortie(
            @ModelAttribute BonsortieDTO bonSortieDTO,
            @RequestParam("materielIds") List<Long> materielIds,
            RedirectAttributes redirectAttributes,HttpSession session) {

              User loggedInUser = (User) session.getAttribute("loggedInUser");


        try {
            // Create Bon de Sortie
            bonSortieService.create(bonSortieDTO, materielIds);

            // Update Materiel status to OUT_OF_STOCK
            for (Long materielId : materielIds) {
                MaterielDTO materiel = materielService.get(materielId);
                if (materiel.getStatus() != Status.OUT_OF_STOCK) {
                    materiel.setStatus(Status.OUT_OF_STOCK);
                    materielService.update(materielId, materiel, null); // null for image file
                }
            }

            redirectAttributes.addFlashAttribute("successMessage", "Bon de Sortie ajouté avec succès !");
            logService.createLog(loggedInUser,"create a  new stock reveal with th id : "+bonSortieDTO.getNumero());
            return "redirect:/responsable/bonSortie";

        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Matériel non trouvé : " + e.getMessage());
            return "redirect:/responsable/bonSortie";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'ajout du Bon de Sortie : " + e.getMessage());
            return "redirect:/responsable/bonSortie";
        }
    }


    @GetMapping("/reservations")
    public String getReservations(Model model){
        List<Reservation> reservationsList = reservationService.findAll();
        model.addAttribute("reservationsList",reservationsList);
        return "home/allReservations";
    }

    @PostMapping("/reservation/accept/{reservationId}")
    public String acceptReservation(@PathVariable Long reservationId, RedirectAttributes redirectAttributes,HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        Reservation updatedReservation = reservationService.updateReservationStatus(reservationId, Reservation.ReservationStatus.ACCEPTEE);
        logService.createLog(loggedInUser,"accepting the reservation with id :"+ reservationId);


        return "redirect:/responsable/reservations";
    }

    // Add the "decline" method
    @PostMapping("/reservation/decline/{reservationId}")
    public String declineReservation(@PathVariable Long reservationId, RedirectAttributes redirectAttributes,HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");


        reservationService.updateReservationStatus(reservationId, Reservation.ReservationStatus.REFUSEE);

        redirectAttributes.addFlashAttribute("message", "Reservation declined.");
        logService.createLog(loggedInUser,"rejecting the reservation with id: "+reservationId);

        return "redirect:/responsable/reservations";
    }





}
