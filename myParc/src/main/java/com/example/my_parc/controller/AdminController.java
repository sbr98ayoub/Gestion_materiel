package com.example.my_parc.controller;

import com.example.my_parc.domain.Materiel;
import com.example.my_parc.domain.User;
import com.example.my_parc.model.Role;
import com.example.my_parc.model.Status;
import com.example.my_parc.model.UserDTO;
import com.example.my_parc.service.LogService;
import com.example.my_parc.service.MaterielService;
import com.example.my_parc.service.ReservationService;
import com.example.my_parc.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private  final ReservationService reservationService ;
    private final MaterielService materielService ;
    private final LogService logService;

    @Autowired
    public AdminController(UserService userService, ReservationService reservationService, MaterielService materielService, LogService logService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.materielService = materielService;
        this.logService = logService;
    }
    @ModelAttribute("loggedInUser")
    public User getLoggedInUser(HttpSession session) {

        return (User) session.getAttribute("loggedInUser");
    }
    @GetMapping
    public String showDashboard(@RequestParam(name = "month", defaultValue = "9") int month,
                                @RequestParam(name = "year", defaultValue = "2024") int year,
                                Model model,HttpSession session) throws JsonProcessingException {
        // Check if the session contains a logged-in user
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equals(loggedInUser.getRole().toString())) {
            // Redirect to the login page if no user is logged in or if the user is not an admin
            return "redirect:/";
        }
        // Fetch most demanded materials

        Map<Materiel, Long> mostDemandedMaterials = reservationService.getMostDemandedMaterials();
        Map<String, Long> mostDemandedMaterialsWithLibelle = mostDemandedMaterials.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().getLibelle(),
                        Map.Entry::getValue,
                        (existingValue, newValue) -> existingValue + newValue
                ));


        // Convert data to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String mostDemandedMaterialsJson = objectMapper.writeValueAsString(mostDemandedMaterialsWithLibelle);

        // Fetch reservations by day in selected month and year
        Map<Integer, Long> reservationsByDay = reservationService.getReservationsByDayInMonth(month, year);
        String reservationsByDayJson = objectMapper.writeValueAsString(reservationsByDay);
        System.out.println("reservationByDay is "+reservationsByDay);
        System.out.println("reservation by day json is "+reservationsByDayJson);
        // Fetch material type distribution
        Map<String, Long> materielTypeCounts = materielService.countMaterielsByType();

        // Stock data
        long inStock = materielService.countByStatus(Status.IN_STOCK);
        long outOfStock = materielService.countByStatus(Status.OUT_OF_STOCK);
        int number = materielService.findAll().size();
        int numberOfDemands= reservationService.findAll().size();
        System.out.println("number of materiels i s"+number);
        // Add attributes to the model
        model.addAttribute("mostDemandedMaterials", mostDemandedMaterialsJson);
        model.addAttribute("reservationsByDay", reservationsByDayJson);
        model.addAttribute("materielTypeCounts", materielTypeCounts);
        model.addAttribute("inStock", inStock);
        model.addAttribute("outOfStock", outOfStock);
        model.addAttribute("materialCount",number);
        model.addAttribute("demandscount",numberOfDemands);


        return "home/admin";
    }
    @GetMapping("/export_csv")
    public void exportCSV(HttpServletResponse response, HttpSession session) throws IOException {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        String filename = "users.csv";
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        CSVWriter writer = new CSVWriter(response.getWriter());
        String[] header = { "User ID", "Username", "Email", "Role" };
        writer.writeNext(header);

        List<UserDTO> users = userService.findAll(); // Fetching users from your service
        for (UserDTO user : users) {
            writer.writeNext(new String[]{String.valueOf(user.getId()), user.getNom() + " " + user.getPrenom(), user.getEmail(), user.getRole().toString()});
        }
        writer.close();
        logService.createLog(loggedInUser,"exporting users in CSV");
    }
    @GetMapping("/export_pdf")
    public void exportPDF(HttpServletResponse response,HttpSession session) throws IOException, DocumentException {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"users.pdf\"");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        PdfPTable table = new PdfPTable(4); // 4 columns
        table.addCell("User ID");
        table.addCell("Username");
        table.addCell("Email");
        table.addCell("Role");

        List<UserDTO> users = userService.findAll();
        for (UserDTO user : users) {
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getNom() + " " + user.getPrenom());
            table.addCell(user.getEmail());
            table.addCell(user.getRole().toString());
        }

        document.add(table);
        document.close();
        logService.createLog(loggedInUser,"exporting users in pdf");
    }


    @GetMapping("/manage_users")
    public String manageUsers(Model model,HttpSession session,@ModelAttribute("successMessage") String successMessage, @ModelAttribute("errorMessage") String errorMessage) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equals(loggedInUser.getRole().toString())) {
            return "redirect:/";
        }
        model.addAttribute("users", userService.findAll());
        model.addAttribute("userDTO", new UserDTO());
        // Check if the flash attributes exist and add them to the model
        if (model.containsAttribute("successMessage")) {
            model.addAttribute("successMessage", model.asMap().get("successMessage"));
        } else {
            model.addAttribute("successMessage", "");
        }

        if (model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", model.asMap().get("errorMessage"));
        } else {
            model.addAttribute("errorMessage", "");
        }
        return "admin/manage_users";
    }

    @GetMapping("/check_email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean emailExists = userService.emailExists(email);
        return ResponseEntity.ok(emailExists);
    }

    @PostMapping("/manage_users/add")
    public String addUser(UserDTO userDTO, RedirectAttributes redirectAttributes ,HttpSession session) {
        User loggedInUser = getLoggedInUser(session);
        if (userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            userDTO.setRole(Role.valueOf(userDTO.getRole().toString().toUpperCase())); // Convert string to enum
            userService.create(userDTO);
            logService.createLog(loggedInUser, "Added new user: " + userDTO.getNom() + " " + userDTO.getPrenom());

            redirectAttributes.addFlashAttribute("successMessage", "User added successfully!");
            return "redirect:/admin/manage_users";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match!");
            return "redirect:/admin/manage_users";
        }
    }


    @PostMapping("/manage_users/delete/{id}")
    public String deleteUser(@PathVariable Long id ,Model model,HttpSession session,RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        userService.delete(id);
        logService.createLog(loggedInUser,"deleting user with id  "+id );
        session.setAttribute("successMessage","users successfully deleted");
        redirectAttributes.addFlashAttribute("successMessage", "User successfully deleted.");
        return "redirect:/admin/manage_users";
    }

    @GetMapping("/manage_users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {


        // Fetch the updated user data from the database
        UserDTO user = userService.get(id);

        System.out.println(user);

        if (user == null) {
            System.out.println("User not found");
            return "redirect:/admin/manage_users?error=userNotFound";
        }

        // Add the updated user data to the model
        model.addAttribute("user", user);
        model.addAttribute("successMessage","user updated successfully");

        // Return the view that will display the edit form with the updated data
        return "admin/edit_users";
    }



    @PostMapping("/manage_users/edit/{id}")
    public String editUser(@PathVariable Long id, @RequestParam Map<String, String> params, Model model,HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setNom(params.get("nom"));
        userDTO.setPrenom(params.get("prenom"));
        userDTO.setEmail(params.get("email"));
        userDTO.setPassword(params.get("password"));

        // Convert the role string to uppercase and map it to the Role enum
        String roleString = params.get("role").toUpperCase();
        try {
            userDTO.setRole(Role.valueOf(roleString));
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid role provided.");
            return "redirect:/admin/manage_users/edit/" + id;
        }

        System.out.println("UserDTO: " + userDTO);  // Debugging line

        userService.update(id, userDTO);
        logService.createLog(loggedInUser,"editing the user "+ userDTO.getNom()+" "+userDTO.getPrenom());
        System.out.println("user updated successfully");

        // Set the success message
        redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");

        return "redirect:/admin/manage_users";
    }

    // Import CSV Users
    @PostMapping("/manage_users/import")
    public String importUsers(@RequestParam("file") MultipartFile file,HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        try {
            userService.importUsers(file);
            logService.createLog(loggedInUser,"importing users");
            redirectAttributes.addFlashAttribute("successMessage", "Users imported successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to import users. Error: " + e.getMessage());
        }
        return "redirect:/admin/manage_users";
    }

    @GetMapping("/reports")
    public String getReports(Model model,HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equals(loggedInUser.getRole().toString())) {
            // Redirect to the login page if no user is logged in or if the user is not an admin
            return "redirect:/";
        }

        return "admin/reports";
    }


    @GetMapping("/settings")
    public String getSettings(Model model,HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equals(loggedInUser.getRole().toString())) {
            // Redirect to the login page if no user is logged in or if the user is not an admin
            return "redirect:/";
        }
        return  "admin/settings";
    }
    @GetMapping("/logs")
    public String viewLogs(Model model, HttpSession session) {
        User loggedInUser = getLoggedInUser(session);
        if (loggedInUser == null || !"ADMIN".equals(loggedInUser.getRole().toString())) {
            return "redirect:/";
        }

        // Fetch logs and pass them to the model
        model.addAttribute("logs", logService.findAllLogs());
        return "admin/logs";  // The view for logs should be named "logs.html"
    }








}
