package com.aiprteam.backend.service;

import com.aiprteam.backend.dto.ai.AIContextDTO;
import com.aiprteam.backend.entity.PullRequest;

public interface PRContextService {
    AIContextDTO buildContext(PullRequest pullRequest, String diff);
}
