package com.aiprteam.backend.service.impl;

import com.aiprteam.backend.config.AiConfig;
import com.aiprteam.backend.exception.AIServiceException;
import com.aiprteam.backend.service.AIClientService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AIClientServiceImpl implements AIClientService {

    private final AiConfig aiConfig;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public AIClientServiceImpl(
            AiConfig aiConfig,
            ObjectMapper objectMapper
    ) {

        this.aiConfig = aiConfig;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder().build();
    }

    @Override
    public String callAi(String prompt) {

        try {

            // OpenAI/Groq request body
            Map<String, Object> body = Map.of(
                    "model", aiConfig.getModel(),
                    "messages", List.of(
                            Map.of(
                                    "role", "user",
                                    "content", prompt
                            )
                    ),
                    "temperature", 0.1,
                    "max_tokens", 4096
            );

            JsonNode response = restClient.post()
                    .uri(aiConfig.getApiUrl())
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            "Bearer " + aiConfig.getApiKey()
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null) {

                throw new AIServiceException(
                        "Empty response from AI provider"
                );
            }

            JsonNode choices = response.path("choices");

            if (!choices.isArray() || choices.isEmpty()) {

                throw new AIServiceException(
                        "No AI response choices returned"
                );
            }

            String text = choices
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            return cleanJson(text);

        } catch (AIServiceException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new AIServiceException(
                    "Unable to call AI provider",
                    ex
            );
        }
    }

    /**
     * Removes markdown wrappers from AI JSON responses.
     */
    private String cleanJson(String text) {

        if (text == null) {
            return "";
        }

        return text
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }

    /**
     * Mock response fallback.
     */
    private String buildMockResponse(String prompt) {

        return """
                {
                  "score": 65,
                  "summary": "Mock AI review generated.",
                  "status": "NEEDS_CHANGES",
                  "missingRequirements": [
                    "Configure a real AI API key."
                  ],
                  "inlineComments": [
                    {
                      "file": "README.md",
                      "line": 1,
                      "comment": "Mock inline comment.",
                      "severity": "INFO"
                    }
                  ]
                }
                """;
    }
}