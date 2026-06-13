package com.aiprteam.backend.service;

import com.aiprteam.backend.dto.github.GithubPRDTO;
import com.aiprteam.backend.dto.github.GithubWebhookDTO;

public interface GithubService {
    void validateWebhookSignature(String payload, String signature);

    GithubWebhookDTO parseWebhookPayload(String payload);

    GithubPRDTO fetchPullRequest(String repoFullName, Integer prNumber);

    String fetchPullRequestDiff(String repoFullName, Integer prNumber);

    boolean validateRepositoryUrl(String repoUrl);
}
