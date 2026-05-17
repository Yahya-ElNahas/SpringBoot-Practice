package com.practice.main.llm.controller;

import com.practice.main.common.response.ApiResponse;
import com.practice.main.llm.application.LlmService;
import com.practice.main.llm.application.dto.PromptRequest;
import com.practice.main.llm.application.dto.PromptResponse;
import com.practice.main.security.principal.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/llm")
@RequiredArgsConstructor
public class LlmController {

    private final LlmService llmService;

    @PostMapping
    public ResponseEntity<ApiResponse<PromptResponse>> sendPrompt(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody PromptRequest request
    ) {
        PromptResponse response = llmService.sendPrompt(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
