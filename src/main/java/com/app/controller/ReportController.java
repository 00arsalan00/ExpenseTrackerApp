package com.app.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.app.model.*;
import com.app.service.ReportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyReportDto>> monthly(
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                reportService.getMonthlyReport(userId)
        );
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryReportDto>> category(
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                reportService.getCategoryReport(userId)
        );
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDto> dashboard(
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                reportService.getDashboard(userId)
        );   
    }
    
    
}
