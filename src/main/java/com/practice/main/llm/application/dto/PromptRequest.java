package com.practice.main.llm.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PromptRequest(
        @NotBlank(message = "{required.prompt}")
        @Size(max = 1000, message = "{invalid.prompt_length}")
        String prompt
) {}