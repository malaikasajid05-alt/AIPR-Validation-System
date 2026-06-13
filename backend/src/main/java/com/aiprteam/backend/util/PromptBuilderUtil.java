package com.aiprteam.backend.util;

import com.aiprteam.backend.dto.ai.AIContextDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class PromptBuilderUtil {

    private final ObjectMapper objectMapper;

    public PromptBuilderUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String buildValidationPrompt(AIContextDTO context) {

        String template = readPrompt("prompts/validation-prompt.txt");

        String contextJson = toJson(context);

        return """
            You are an expert AI code reviewer.

            Analyze this pull request carefully.

            Return ONLY valid JSON.

            Do NOT return markdown.
            Do NOT use ```json.

            Format:

            {
              "score": number,
              "summary": "string",
              "status": "APPROVED|NEEDS_CHANGES",
              "missingRequirements": [],
              "inlineComments": [
                {
                  "file": "string",
                  "line": number,
                  "comment": "string",
                  "severity": "LOW|MEDIUM|HIGH"
                }
              ]
            }

            Additional Instructions:
            """
                + System.lineSeparator()
                + template
                + System.lineSeparator()
                + System.lineSeparator()
                + "Pull Request Context:"
                + System.lineSeparator()
                + contextJson;
    }

    private String readPrompt(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to read prompt template: " + path, ex);
        }
    }

    private String toJson(AIContextDTO context) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(context);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Unable to serialize AI context", ex);
        }
    }
}
