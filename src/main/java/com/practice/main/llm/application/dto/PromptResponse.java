package com.practice.main.llm.application.dto;

import com.practice.main.llm.application.dto.internal.PromptProductResponse;

import java.util.List;

public record PromptResponse(String message, List<PromptProductResponse> products) {}