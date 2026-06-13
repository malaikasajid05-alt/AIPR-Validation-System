package com.aiprteam.backend.util;

import com.aiprteam.backend.dto.ai.AIResponseDTO;
import com.aiprteam.backend.dto.ai.InlineCommentDTO;
import com.aiprteam.backend.entity.PullRequest;
import com.aiprteam.backend.exception.AIServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AIResponseParserUtil {

    private final ObjectMapper objectMapper;

    public AIResponseParserUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public AIResponseDTO parse(String rawResponse) {
        try {
            String json = extractJson(rawResponse);
            JsonNode root = objectMapper.readTree(json);

            if (!root.hasNonNull("score") || !root.hasNonNull("summary") || !root.hasNonNull("status")) {
                throw new AIServiceException("AI response missing required fields");
            }

            AIResponseDTO response = new AIResponseDTO();
            response.setScore(root.get("score").asInt());
            response.setSummary(root.get("summary").asText());
            response.setStatus(parseStatus(root.get("status").asText()));
            response.setMissingRequirements(parseStringArray(root.path("missingRequirements")));
            response.setInlineComments(parseInlineComments(root.path("inlineComments")));
            return response;
        } catch (AIServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AIServiceException("Unable to parse AI response", ex);
        }
    }

    private PullRequest.ReviewStatus parseStatus(String status) {
        try {
            return PullRequest.ReviewStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new AIServiceException("Invalid AI status: " + status);
        }
    }

    private List<String> parseStringArray(JsonNode node) {
        List<String> values = new ArrayList<>();
        if (node.isArray()) {
            Iterator<JsonNode> iterator = node.elements();
            while (iterator.hasNext()) {
                values.add(iterator.next().asText());
            }
        }
        return values;
    }

    private List<InlineCommentDTO> parseInlineComments(JsonNode node) {
        List<InlineCommentDTO> comments = new ArrayList<>();
        if (!node.isArray()) {
            return comments;
        }

        for (JsonNode commentNode : node) {
            if (!commentNode.hasNonNull("file")
                    || !commentNode.hasNonNull("line")
                    || !commentNode.hasNonNull("comment")
                    || !commentNode.hasNonNull("severity")) {
                throw new AIServiceException("Inline comment missing required fields");
            }
            InlineCommentDTO comment = new InlineCommentDTO();
            comment.setFile(commentNode.get("file").asText());
            comment.setLine(commentNode.get("line").asInt());
            comment.setComment(commentNode.get("comment").asText());
            comment.setSeverity(commentNode.get("severity").asText());
            comments.add(comment);
        }
        return comments;
    }

    private String extractJson(String rawResponse) {
        String trimmed = rawResponse == null ? "" : rawResponse.trim();
        if (trimmed.startsWith("```")) {
            int start = trimmed.indexOf('{');
            int end = trimmed.lastIndexOf('}');
            if (start >= 0 && end > start) {
                return trimmed.substring(start, end + 1);
            }
        }
        int start = trimmed.indexOf('{');
        int end = trimmed.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return trimmed.substring(start, end + 1);
        }
        throw new AIServiceException("AI response does not contain JSON");
    }
}
