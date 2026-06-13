package com.aiprteam.backend.service;

import com.aiprteam.backend.dto.github.GithubWebhookDTO;

public interface GithubWebhookProcessor {
    void processPullRequestEvent(GithubWebhookDTO webhook);
}
