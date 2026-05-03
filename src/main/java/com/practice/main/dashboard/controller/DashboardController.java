package com.practice.main.dashboard.controller;

import com.practice.main.common.response.ApiResponse;
import com.practice.main.dashboard.application.DashboardService;
import com.practice.main.dashboard.application.dto.request.TaskRequest;
import com.practice.main.dashboard.application.dto.response.DashboardResponse;
import com.practice.main.dashboard.application.dto.response.TaskResponse;
import com.practice.main.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

    @PostMapping("execute-tasks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> executeTasks(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody List<TaskRequest> requestBody
    ) {
        List<TaskResponse> result = dashboardService.executeAllTasks(userPrincipal, requestBody);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
