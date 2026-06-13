package com.aiprteam.backend.service;

import com.aiprteam.backend.dto.ai.AIResponseDTO;
import com.aiprteam.backend.entity.PullRequest;

public interface AIValidationService {
    AIResponseDTO validate(PullRequest pullRequest, String diff);
}
