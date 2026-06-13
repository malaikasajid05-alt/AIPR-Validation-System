package com.aiprteam.backend.service.impl;

import com.aiprteam.backend.dto.ai.AIResponseDTO;
import com.aiprteam.backend.dto.github.GithubPRDTO;
import com.aiprteam.backend.dto.github.GithubWebhookDTO;
import com.aiprteam.backend.entity.PullRequest;
import com.aiprteam.backend.service.AIValidationService;
import com.aiprteam.backend.service.GitHubCommentService;
import com.aiprteam.backend.service.GithubService;
import com.aiprteam.backend.service.GithubWebhookProcessor;
import com.aiprteam.backend.service.PrService;
import com.aiprteam.backend.util.GithubRepoUrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class GithubWebhookProcessorImpl implements GithubWebhookProcessor {

    private static final Logger log = LoggerFactory.getLogger(GithubWebhookProcessorImpl.class);

    private final GithubService githubService;
    private final PrService prService;
    private final AIValidationService aiValidationService;
    private final GitHubCommentService gitHubCommentService;

    public GithubWebhookProcessorImpl(
            GithubService githubService,
            PrService prService,
            AIValidationService aiValidationService,
            GitHubCommentService gitHubCommentService) {
        this.githubService = githubService;
        this.prService = prService;
        this.aiValidationService = aiValidationService;
        this.gitHubCommentService = gitHubCommentService;
    }

    @Override
    @Async
    public void processPullRequestEvent(GithubWebhookDTO webhook) {
        String repoFullName = webhook.getRepoFullName();
        Integer prNumber = webhook.getPrNumber();
        try {
            if (webhook.getRepoUrl() != null && !webhook.getRepoUrl().isBlank()) {
                webhook.setRepoUrl(GithubRepoUrlUtil.normalize(webhook.getRepoUrl()));
            }

            log.info("Processing PR webhook: {} PR #{} action={}", repoFullName, prNumber, webhook.getAction());

            String diff = githubService.fetchPullRequestDiff(repoFullName, prNumber);
            PullRequest pullRequest = prService.createOrUpdateFromWebhook(webhook, diff);
            AIResponseDTO aiResponse = aiValidationService.validate(pullRequest, diff);
            GithubPRDTO githubPR = githubService.fetchPullRequest(repoFullName, prNumber);
            githubPR.setDiff(diff);
            gitHubCommentService.postReviewComments(pullRequest, githubPR, aiResponse);
            prService.saveValidationResult(pullRequest, aiResponse);

            log.info(
                    "Completed PR validation for {} PR #{}: status={} score={}",
                    repoFullName,
                    prNumber,
                    aiResponse.getStatus(),
                    aiResponse.getScore());
        } catch (Exception ex) {
            log.error(
                    "Failed to process PR webhook for {} PR #{}: {}",
                    repoFullName,
                    prNumber,
                    ex.getMessage(),
                    ex);
            throw ex;
        }
    }
}
