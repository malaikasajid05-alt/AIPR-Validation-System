package com.aiprteam.backend.service.impl;

import com.aiprteam.backend.config.GithubConfig;
import com.aiprteam.backend.dto.github.GithubPRDTO;
import com.aiprteam.backend.dto.github.GithubWebhookDTO;
import com.aiprteam.backend.exception.GithubException;
import com.aiprteam.backend.service.GithubService;
import com.aiprteam.backend.util.GithubRepoPathUtil;
import com.aiprteam.backend.util.GithubRepoPathUtil.RepoPath;
import com.aiprteam.backend.util.GithubRepoUrlUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class GithubServiceImpl implements GithubService {

    private final GithubConfig githubConfig;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public GithubServiceImpl(GithubConfig githubConfig, ObjectMapper objectMapper) {
        this.githubConfig = githubConfig;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder()
                .baseUrl(githubConfig.getApiBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }

    @Override
    public void validateWebhookSignature(String payload, String signature) {
        if (signature == null || signature.isBlank()) {
            throw new GithubException("Missing GitHub signature");
        }
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                    githubConfig.getWebhookSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            String expected = "sha256=46d31724eccfd27e2d837b8f45d259fd4a68a6b0a8e4ff638d56963e855648bf";
            if (!expected.equals(signature)) {
                throw new GithubException("Invalid GitHub signature");
            }
        } catch (GithubException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new GithubException("Unable to validate GitHub signature", ex);
        }
    }

    @Override
    public GithubWebhookDTO parseWebhookPayload(String payload) {
        try {
            JsonNode root = objectMapper.readTree(payload);
            String action = root.path("action").asText();
            JsonNode pullRequest = root.path("pull_request");
            JsonNode repository = root.path("repository");

            if (pullRequest.isMissingNode() || repository.isMissingNode()) {
                throw new GithubException("Webhook payload is not a pull request event");
            }

            GithubWebhookDTO dto = new GithubWebhookDTO();
            dto.setAction(action);
            dto.setRepoFullName(repository.path("full_name").asText());
            dto.setRepoUrl(repository.path("html_url").asText());
            dto.setPrNumber(pullRequest.path("number").asInt());
            dto.setBranch(pullRequest.path("head").path("ref").asText());
            dto.setDiffUrl(pullRequest.path("diff_url").asText());
            dto.setPrTitle(pullRequest.path("title").asText());
            dto.setPrHtmlUrl(pullRequest.path("html_url").asText());
            return dto;
        } catch (GithubException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new GithubException("Unable to parse GitHub webhook payload", ex);
        }
    }

    @Override
    public GithubPRDTO fetchPullRequest(String repoFullName, Integer prNumber) {
        RepoPath path = GithubRepoPathUtil.parseRepoFullName(repoFullName);
        try {
            JsonNode response = restClient.get()
                    .uri("/repos/{owner}/{repo}/pulls/{prNumber}", path.owner(), path.repo(), prNumber)
                    .headers(this::applyAuth)
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null) {
                throw new GithubException("GitHub returned empty pull request response");
            }

            GithubPRDTO dto = new GithubPRDTO();
            dto.setNumber(response.path("number").asInt());
            dto.setTitle(response.path("title").asText());
            dto.setBranch(response.path("head").path("ref").asText());
            dto.setRepoFullName(repoFullName);
            dto.setRepoUrl(response.path("base").path("repo").path("html_url").asText());
            dto.setDiffUrl(response.path("diff_url").asText());
            dto.setHtmlUrl(response.path("html_url").asText());
            return dto;
        } catch (GithubException ex) {
            throw ex;
        } catch (Exception ex) {
            throw wrapGitHubApiError("Unable to fetch pull request from GitHub", repoFullName, prNumber, ex);
        }
    }

    @Override
    public String fetchPullRequestDiff(String repoFullName, Integer prNumber) {
        RepoPath path = GithubRepoPathUtil.parseRepoFullName(repoFullName);
        try {
            return restClient.get()
                    .uri("/repos/{owner}/{repo}/pulls/{prNumber}", path.owner(), path.repo(), prNumber)
                    .headers(headers -> {
                        applyAuth(headers);
                        headers.setAccept(List.of(MediaType.parseMediaType("application/vnd.github.v3.diff")));
                    })
                    .retrieve()
                    .body(String.class);
        } catch (Exception ex) {
            throw wrapGitHubApiError("Unable to fetch pull request diff from GitHub", repoFullName, prNumber, ex);
        }
    }

    @Override
    public boolean validateRepositoryUrl(String repoUrl) {
        String repoFullName = GithubRepoUrlUtil.extractFullName(repoUrl);
        if (githubConfig.getApiToken() == null || githubConfig.getApiToken().isBlank()) {
            return true;
        }
        RepoPath path = GithubRepoPathUtil.parseRepoFullName(repoFullName);
        try {
            restClient.get()
                    .uri("/repos/{owner}/{repo}", path.owner(), path.repo())
                    .headers(this::applyAuth)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private void applyAuth(HttpHeaders headers) {
        if (githubConfig.getApiToken() != null && !githubConfig.getApiToken().isBlank()) {
            headers.setBearerAuth(githubConfig.getApiToken());
        }
    }

    private GithubException wrapGitHubApiError(String message, String repoFullName, Integer prNumber, Exception ex) {
        if (ex instanceof HttpClientErrorException.NotFound) {
            String hint = (githubConfig.getApiToken() == null || githubConfig.getApiToken().isBlank())
                    ? " Set GITHUB_API_TOKEN for private repositories."
                    : " Verify the token has access to this repository and the pull request exists.";
            return new GithubException(
                    message + " (" + repoFullName + " PR #" + prNumber + ")." + hint, ex);
        }
        return new GithubException(message, ex);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            builder.append(String.format("%02x", value));
        }
        return builder.toString();
    }
}
