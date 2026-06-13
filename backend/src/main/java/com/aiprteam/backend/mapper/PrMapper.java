package com.aiprteam.backend.mapper;

import com.aiprteam.backend.dto.ai.InlineCommentDTO;
import com.aiprteam.backend.dto.pr.PullRequestDTO;
import com.aiprteam.backend.dto.pr.PullRequestResponseDTO;
import com.aiprteam.backend.entity.PullRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PrMapper {

    private final ObjectMapper objectMapper;

    public PrMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public PullRequestDTO toDto(PullRequest pullRequest) {
        PullRequestDTO dto = new PullRequestDTO();
        dto.setId(pullRequest.getId());
        dto.setProjectId(pullRequest.getProject().getId());
        dto.setPrNumber(pullRequest.getPrNumber());
        dto.setTitle(pullRequest.getTitle());
        dto.setBranch(pullRequest.getBranch());
        dto.setRepoUrl(pullRequest.getRepoUrl());
        dto.setGithubPrUrl(pullRequest.getGithubPrUrl());
        dto.setScore(pullRequest.getScore());
        dto.setAiSummary(pullRequest.getAiSummary());
        dto.setStatus(pullRequest.getStatus());
        return dto;
    }

    public PullRequestResponseDTO toResponseDto(PullRequest pullRequest) {
        PullRequestResponseDTO dto = new PullRequestResponseDTO();
        dto.setId(pullRequest.getId());
        dto.setProjectId(pullRequest.getProject().getId());
        dto.setProjectName(pullRequest.getProject().getName());
        dto.setPrNumber(pullRequest.getPrNumber());
        dto.setTitle(pullRequest.getTitle());
        dto.setBranch(pullRequest.getBranch());
        dto.setRepoUrl(pullRequest.getRepoUrl());
        dto.setGithubPrUrl(pullRequest.getGithubPrUrl());
        dto.setScore(pullRequest.getScore());
        dto.setAiSummary(pullRequest.getAiSummary());
        dto.setStatus(pullRequest.getStatus());
        dto.setMissingRequirements(readStringList(pullRequest.getMissingRequirementsJson()));
        dto.setInlineComments(readInlineComments(pullRequest.getInlineCommentsJson()));
        return dto;
    }

    private List<String> readStringList(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    private List<InlineCommentDTO> readInlineComments(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<InlineCommentDTO>>() {});
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }
}
