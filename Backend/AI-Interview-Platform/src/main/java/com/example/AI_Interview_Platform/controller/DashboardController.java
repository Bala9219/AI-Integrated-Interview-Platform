package com.example.AI_Interview_Platform.controller;

import com.example.AI_Interview_Platform.DTO.DashboardResponse;
import com.example.AI_Interview_Platform.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/api/dashboard")
    public DashboardResponse getDashboard(){
        return dashboardService.buildDashboard();
    }
}
