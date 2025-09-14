package com.limtic.lab.controller;

import com.limtic.lab.dto.DashboardFeed;
import com.limtic.lab.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardFeed getDashboard(@RequestParam(defaultValue = "5") int latestFilesCount) {
        return dashboardService.getDashboardFeed(latestFilesCount);
    }
}
