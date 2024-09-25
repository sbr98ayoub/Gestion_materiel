package com.example.my_parc.controller;

import com.example.my_parc.model.MaterielDTO;
import com.example.my_parc.service.MaterielService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
//@RequestMapping("/responsable")
public class MaterielController {
//
//    private final MaterielService materielService;
//
//    @Autowired
//    public MaterielController(MaterielService materielService) {
//        this.materielService = materielService;
//    }
//
//    @GetMapping("/materiel")
//    public String showMaterielList(Model model) {
//        List<MaterielDTO> materielList = materielService.findAll();
//        model.addAttribute("materielList", materielList);
//        return "home/responsable"; // Returns the view 'responsable.html' inside the 'home' directory
//    }
//
//    @GetMapping("materiel/add")
//    public String showAddMaterielForm(Model model) {
//        model.addAttribute("materielDTO", new MaterielDTO());
//        return "home/responsable"; // Returns the view 'responsable.html' inside the 'home' directory
//    }
//
//    @PostMapping("materiel/add")
//    public String addMateriel(
//            @ModelAttribute MaterielDTO materielDTO,
//            @RequestParam("imageFile") MultipartFile imageFile,
//            RedirectAttributes redirectAttributes) {
//
//        try {
//            materielService.create(materielDTO, imageFile);
//            redirectAttributes.addFlashAttribute("successMessage", "Matériel ajouté avec succès !");
//            return "redirect:/responsable/materiel"; // Redirect to the main page to show the success message
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'ajout du matériel.");
//            return "redirect:/responsable/materiel"; // Redirect to the main page to show the error message
//        }
//    }
//
//    // New method to show the edit form
//    @GetMapping("materiel/edit/{id}")
//    public String showEditMaterielForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
//        try {
//            MaterielDTO materielDTO = materielService.get(  id);
//            model.addAttribute("materielDTO", materielDTO);
//            return "home/responsable"; // Returns the view 'responsable.html'
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la récupération du matériel.");
//            return "redirect:/responsable/materiel";
//        }
//    }
//
//    // New method to handle the edit submission
//    @PostMapping("materiel/edit/{id}")
//    public String editMateriel(
//            @PathVariable Long id,
//            @ModelAttribute MaterielDTO materielDTO,
//            @RequestParam("imageFile") MultipartFile imageFile,
//            RedirectAttributes redirectAttributes) {
//
//        try {
//            materielService.update(id, materielDTO, imageFile);
//            redirectAttributes.addFlashAttribute("successMessage", "Matériel mis à jour avec succès !");
//            return "redirect:/responsable/materiel";
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la mise à jour du matériel.");
//            return "redirect:/responsable/materiel";
//        }
//    }
//
//    // New method to handle the deletion
//    @PostMapping("materiel/delete/{id}")
//    public String deleteMateriel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
//        try {
//            materielService.delete(id);
//            redirectAttributes.addFlashAttribute("successMessage", "Matériel supprimé avec succès !");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression du matériel.");
//        }
//        return "redirect:/responsable/materiel";
//    }


}

