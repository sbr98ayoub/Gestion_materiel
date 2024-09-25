package com.example.my_parc.controller;

import com.example.my_parc.service.ReportService;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // For exporting the user report to PDF
    @GetMapping("/users")
    public void generateUserReport(HttpServletResponse response) throws Exception {
        // Compile the report
        JasperReport jasperReport = reportService.compileReport("user_reports.jrxml");

        // Fill the report (with no parameters for now)
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = reportService.fillReport(jasperReport, parameters);

        // Export the report to PDF
        reportService.exportToPDF(jasperPrint, response);
    }

    // For exporting the user report to CSV
    @GetMapping("/users/csv")
    public void generateUserReportCSV(HttpServletResponse response) throws Exception {
        // Compile the report
        JasperReport jasperReport = reportService.compileReport("user_reports.jrxml");

        // Fill the report (with no parameters for now)
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = reportService.fillReport(jasperReport, parameters);

        // Export the report to CSV
        reportService.exportToCSV(jasperPrint, response);
    }
    @GetMapping("/materials")
    public void generateMaterialsReport(HttpServletResponse response) throws Exception {
        // Compile the report
        JasperReport jasperReport = reportService.compileReport("materials.jrxml");

        // Fill the report
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = reportService.fillReport(jasperReport, parameters);

        // Export the report to PDF
        reportService.exportToPDF(jasperPrint, response);
    }

    // For exporting the materials report to CSV
    @GetMapping("/materials/csv")
    public void generateMaterialsReportCSV(HttpServletResponse response) throws Exception {
        // Compile the report
        JasperReport jasperReport = reportService.compileReport("materials.jrxml");

        // Fill the report
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = reportService.fillReport(jasperReport, parameters);

        // Export the report to CSV
        reportService.exportToCSV(jasperPrint, response);
    }

    // For exporting the reservations report to PDF
    @GetMapping("/reservations")
    public void generateReservationsReport(HttpServletResponse response) throws Exception {
        // Compile the report
        JasperReport jasperReport = reportService.compileReport("reservations.jrxml");

        // Fill the report
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = reportService.fillReport(jasperReport, parameters);

        // Export the report to PDF
        reportService.exportToPDF(jasperPrint, response);
    }

    // For exporting the reservations report to CSV
    @GetMapping("/reservations/csv")
    public void generateReservationsReportCSV(HttpServletResponse response) throws Exception {
        // Compile the report
        JasperReport jasperReport = reportService.compileReport("reservations.jrxml");

        // Fill the report
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = reportService.fillReport(jasperReport, parameters);

        // Export the report to CSV
        reportService.exportToCSV(jasperPrint, response);
    }
    // For exporting the bonsorties report to PDF
    @GetMapping("/bons_sorties")
    public void generatebonsSortiesReport(HttpServletResponse response) throws Exception {
        // Compile the report
        JasperReport jasperReport = reportService.compileReport("bonsorties.jrxml");

        // Fill the report
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = reportService.fillReport(jasperReport, parameters);

        // Export the report to PDF
        reportService.exportToPDF(jasperPrint, response);
    }
    // For exporting the reservations report to CSV
    @GetMapping("/bon_sorties/csv")
    public void generatebonssortiesReportCSV(HttpServletResponse response) throws Exception {
        // Compile the report
        JasperReport jasperReport = reportService.compileReport("bonsorties.jrxml");

        // Fill the report
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = reportService.fillReport(jasperReport, parameters);

        // Export the report to CSV
        reportService.exportToCSV(jasperPrint, response);
    }
}
