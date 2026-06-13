package com.aiprteam.backend.service;

import com.aiprteam.backend.dto.ai.AIResponseDTO;
import com.aiprteam.backend.dto.github.GithubPRDTO;
import com.aiprteam.backend.entity.PullRequest;

public interface GitHubCommentService {
    void postReviewComments(PullRequest pullRequest, GithubPRDTO githubPR, AIResponseDTO aiResponse);
}
