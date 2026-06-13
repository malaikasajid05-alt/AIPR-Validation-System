package com.aiprteam.backend.service;

import com.aiprteam.backend.dto.github.GithubWebhookDTO;
import com.aiprteam.backend.entity.Project;

public interface ProjectResolutionService {
    Project resolveForWebhook(GithubWebhookDTO webhook);
}
