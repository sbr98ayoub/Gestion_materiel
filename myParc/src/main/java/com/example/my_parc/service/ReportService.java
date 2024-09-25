package com.example.my_parc.service;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private DataSource dataSource;

    public JasperReport compileReport(String reportFileName) throws Exception {
        InputStream reportStream = new ClassPathResource("reports/" + reportFileName).getInputStream();
        return JasperCompileManager.compileReport(reportStream);
    }

    public JasperPrint fillReport(JasperReport jasperReport, Map<String, Object> parameters) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            return JasperFillManager.fillReport(jasperReport, parameters, connection);
        }
    }

    public void exportToPDF(JasperPrint jasperPrint, HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=user_report.pdf");  // Changed to "inline"
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }

    public void exportToCSV(JasperPrint jasperPrint, HttpServletResponse response) throws Exception {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "inline; filename=user_report.csv");  // Changed to "inline"

        JRCsvExporter exporter = new JRCsvExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(response.getWriter()));

        SimpleCsvExporterConfiguration configuration = new SimpleCsvExporterConfiguration();
        exporter.setConfiguration(configuration);
        exporter.exportReport();
    }


}
