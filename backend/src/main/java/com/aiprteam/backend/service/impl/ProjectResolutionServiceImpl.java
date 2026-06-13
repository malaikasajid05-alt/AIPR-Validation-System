package com.aiprteam.backend.service.impl;

import com.aiprteam.backend.dto.github.GithubWebhookDTO;
import com.aiprteam.backend.entity.GitHubConnection;
import com.aiprteam.backend.entity.Project;
import com.aiprteam.backend.exception.ResourceNotFoundException;
import com.aiprteam.backend.repository.GitHubConnectionRepository;
import com.aiprteam.backend.repository.ProjectRepository;
import com.aiprteam.backend.service.ProjectResolutionService;
import com.aiprteam.backend.util.GithubRepoUrlUtil;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProjectResolutionServiceImpl implements ProjectResolutionService {

    private static final Logger log = LoggerFactory.getLogger(ProjectResolutionServiceImpl.class);

    private final GitHubConnectionRepository gitHubConnectionRepository;
    private final ProjectRepository projectRepository;

    public ProjectResolutionServiceImpl(
            GitHubConnectionRepository gitHubConnectionRepository, ProjectRepository projectRepository) {
        this.gitHubConnectionRepository = gitHubConnectionRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public Project resolveForWebhook(GithubWebhookDTO webhook) {
        String repoFullName = webhook.getRepoFullName();
        if (repoFullName != null && !repoFullName.isBlank()) {
            List<GitHubConnection> connections =
                    gitHubConnectionRepository.findAllByRepoFullNameIgnoreCase(repoFullName.trim());
            if (connections.size() > 1) {
                log.warn(
                        "Multiple projects linked to repository {}. Using newest project id={}.",
                        repoFullName,
                        connections.stream()
                                .map(c -> c.getProject().getId())
                                .max(Comparator.naturalOrder())
                                .orElse(null));
            }
            if (!connections.isEmpty()) {
                return connections.stream()
                        .max(Comparator.comparing(c -> c.getProject().getId()))
                        .map(GitHubConnection::getProject)
                        .orElseThrow();
            }
        }

        String normalizedUrl = GithubRepoUrlUtil.normalize(webhook.getRepoUrl());
        List<Project> byUrl = projectRepository.findAllByGithubRepoUrlIgnoreCase(normalizedUrl);
        if (byUrl.size() > 1) {
            log.warn(
                    "Multiple projects with github_repo_url {}. Using newest project id={}.",
                    normalizedUrl,
                    byUrl.stream().map(Project::getId).max(Comparator.naturalOrder()).orElse(null));
        }
        if (!byUrl.isEmpty()) {
            return byUrl.stream().max(Comparator.comparing(Project::getId)).orElseThrow();
        }

        throw new ResourceNotFoundException(
                "No project linked to repository: " + repoFullName + " (" + normalizedUrl + ")");
    }
}
