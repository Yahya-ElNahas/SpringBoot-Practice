package com.practice.main.llm.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.main.llm.application.dto.PromptRequest;
import com.practice.main.llm.application.dto.PromptResponse;
import com.practice.main.llm.application.util.ProductTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class LlmService {

    private final OpenAiChatModel model;

    private final ProductTools productTools;

    private final ObjectMapper mapper;

    private final String INSTRUCTIONS = """
            You are a helpful e-commerce assistant.
            Always add the product(s) in JSON format like this:
            {
                "message": "your response here",
                "products": [
                    {"id": "...", "name": "...", "price": 0.00, "stock": 0}
                ]
            }
            Only include "products" array when relevant.
            Never use markdown. Never wrap in code blocks. Return raw JSON only.
            """;

    public PromptResponse sendPrompt(PromptRequest request) {

        String response = ChatClient.create(model)
                .prompt()
                .system(INSTRUCTIONS)
                .user(request.prompt())
                .tools(productTools)
                .call()
                .content();

        try {
            return mapper.readValue(response, PromptResponse.class);
        } catch (JsonProcessingException e) {
            log.error("", e);
            return new PromptResponse(response, null);
        }
    }
}
