package com.aiprteam.backend.service;

import com.aiprteam.backend.dto.ai.AIResponseDTO;
import com.aiprteam.backend.dto.github.GithubWebhookDTO;
import com.aiprteam.backend.dto.pr.PullRequestDTO;
import com.aiprteam.backend.dto.pr.PullRequestResponseDTO;
import com.aiprteam.backend.entity.PullRequest;
import java.util.List;

public interface PrService {
    PullRequest createOrUpdateFromWebhook(GithubWebhookDTO webhook, String diff);

    void saveValidationResult(PullRequest pullRequest, AIResponseDTO aiResponse);

    List<PullRequestResponseDTO> getProjectPullRequests(String ownerEmail, Long projectId);

    List<PullRequestResponseDTO> getAllPullRequests(String ownerEmail);

    PullRequestResponseDTO getPullRequest(String ownerEmail, Long pullRequestId);
}
