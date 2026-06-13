package com.aiprteam.backend.controller;

import com.aiprteam.backend.dto.github.GithubWebhookDTO;
import com.aiprteam.backend.service.GithubService;
import com.aiprteam.backend.service.GithubWebhookProcessor;
import java.util.Map;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/github")
public class GithubController {

    private static final Set<String> SUPPORTED_ACTIONS = Set.of("opened", "synchronize", "reopened");

    private final GithubService githubService;
    private final GithubWebhookProcessor githubWebhookProcessor;

    public GithubController(GithubService githubService, GithubWebhookProcessor githubWebhookProcessor) {
        this.githubService = githubService;
        this.githubWebhookProcessor = githubWebhookProcessor;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Map<String, String>> handleWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature) {
        githubService.validateWebhookSignature(payload, signature);
        GithubWebhookDTO webhook = githubService.parseWebhookPayload(payload);
        if (SUPPORTED_ACTIONS.contains(webhook.getAction())) {
            githubWebhookProcessor.processPullRequestEvent(webhook);
        }
        return ResponseEntity.ok(Map.of("status", "accepted"));
    }
}
