package com.aiprteam.backend.service.impl;

import com.aiprteam.backend.config.GithubConfig;
import com.aiprteam.backend.dto.ai.AIResponseDTO;
import com.aiprteam.backend.dto.ai.InlineCommentDTO;
import com.aiprteam.backend.dto.github.GithubPRDTO;
import com.aiprteam.backend.entity.PullRequest;
import com.aiprteam.backend.exception.GithubException;
import com.aiprteam.backend.service.GitHubCommentService;
import com.aiprteam.backend.util.GithubRepoPathUtil;
import com.aiprteam.backend.util.GithubRepoPathUtil.RepoPath;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GitHubCommentServiceImpl implements GitHubCommentService {

    private final GithubConfig githubConfig;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public GitHubCommentServiceImpl(GithubConfig githubConfig, ObjectMapper objectMapper) {
        this.githubConfig = githubConfig;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder()
                .baseUrl(githubConfig.getApiBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }

    @Override
    public void postReviewComments(PullRequest pullRequest, GithubPRDTO githubPR, AIResponseDTO aiResponse) {
        if (githubConfig.getApiToken() == null || githubConfig.getApiToken().isBlank()) {
            return;
        }

        RepoPath path = GithubRepoPathUtil.parseRepoFullName(githubPR.getRepoFullName());
        try {
            JsonNode prNode = restClient.get()
                    .uri("/repos/{owner}/{repo}/pulls/{prNumber}", path.owner(), path.repo(), githubPR.getNumber())
                    .headers(this::applyAuth)
                    .retrieve()
                    .body(JsonNode.class);

            if (prNode == null) {
                throw new GithubException("GitHub returned empty pull request response");
            }

            String commitId = prNode.path("head").path("sha").asText();
            List<Map<String, Object>> comments = new ArrayList<>();
            for (InlineCommentDTO inlineComment : aiResponse.getInlineComments()) {
                Map<String, Object> comment = new HashMap<>();
                comment.put("path", inlineComment.getFile());
                comment.put("line", inlineComment.getLine());
                comment.put("side", "RIGHT");
                comment.put("body", "[" + inlineComment.getSeverity() + "] " + inlineComment.getComment());
                comments.add(comment);
            }

            Map<String, Object> reviewBody = new HashMap<>();
            reviewBody.put("commit_id", commitId);
            reviewBody.put("body", buildSummaryBody(aiResponse));
            reviewBody.put("event", mapReviewEvent(aiResponse.getStatus()));
            if (!comments.isEmpty()) {
                reviewBody.put("comments", comments);
            }

            restClient.post()
                    .uri(
                            "/repos/{owner}/{repo}/pulls/{prNumber}/reviews",
                            path.owner(),
                            path.repo(),
                            githubPR.getNumber())
                    .headers(this::applyAuth)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(reviewBody)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception ex) {
            throw new GithubException("Unable to post GitHub review comments", ex);
        }
    }

    private String buildSummaryBody(AIResponseDTO aiResponse) {
        StringBuilder builder = new StringBuilder();
        builder.append("AI PR Validation Summary").append(System.lineSeparator());
        builder.append("Score: ").append(aiResponse.getScore()).append(System.lineSeparator());
        builder.append("Status: ").append(aiResponse.getStatus()).append(System.lineSeparator());
        builder.append(aiResponse.getSummary());
        if (aiResponse.getMissingRequirements() != null && !aiResponse.getMissingRequirements().isEmpty()) {
            builder.append(System.lineSeparator()).append("Missing requirements:");
            for (String requirement : aiResponse.getMissingRequirements()) {
                builder.append(System.lineSeparator()).append("- ").append(requirement);
            }
        }
        return builder.toString();
    }

    private String mapReviewEvent(PullRequest.ReviewStatus status) {
        return switch (status) {
            case APPROVED -> "APPROVE";
            case REJECTED -> "REQUEST_CHANGES";
            default -> "COMMENT";
        };
    }

    private void applyAuth(HttpHeaders headers) {
        headers.setBearerAuth(githubConfig.getApiToken());
    }
}
