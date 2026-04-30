package com.practice.main.dashboard.controller;

import com.practice.main.common.response.ApiResponse;
import com.practice.main.dashboard.application.DashboardService;
import com.practice.main.dashboard.application.dto.DashboardResponse;
import com.practice.main.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        DashboardResponse result = dashboardService.getUserDashboard(userPrincipal);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
