package com.aiprteam.backend.service.impl;

import com.aiprteam.backend.dto.ai.AIResponseDTO;
import com.aiprteam.backend.dto.github.GithubWebhookDTO;
import com.aiprteam.backend.dto.pr.PullRequestResponseDTO;
import com.aiprteam.backend.entity.Project;
import com.aiprteam.backend.entity.PullRequest;
import com.aiprteam.backend.entity.Users;
import com.aiprteam.backend.exception.ResourceNotFoundException;
import com.aiprteam.backend.mapper.PrMapper;
import com.aiprteam.backend.repository.ProjectRepository;
import com.aiprteam.backend.repository.PullRequestRepository;
import com.aiprteam.backend.repository.UsersRepository;
import com.aiprteam.backend.service.PrService;
import com.aiprteam.backend.service.ProjectResolutionService;
import com.aiprteam.backend.util.GithubRepoUrlUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrServiceImpl implements PrService {

    private final PullRequestRepository pullRequestRepository;
    private final ProjectRepository projectRepository;
    private final UsersRepository usersRepository;
    private final ProjectResolutionService projectResolutionService;
    private final PrMapper prMapper;
    private final ObjectMapper objectMapper;

    public PrServiceImpl(
            PullRequestRepository pullRequestRepository,
            ProjectRepository projectRepository,
            UsersRepository usersRepository,
            ProjectResolutionService projectResolutionService,
            PrMapper prMapper,
            ObjectMapper objectMapper) {
        this.pullRequestRepository = pullRequestRepository;
        this.projectRepository = projectRepository;
        this.usersRepository = usersRepository;
        this.projectResolutionService = projectResolutionService;
        this.prMapper = prMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public PullRequest createOrUpdateFromWebhook(GithubWebhookDTO webhook, String diff) {
        Project project = projectResolutionService.resolveForWebhook(webhook);
        String normalizedRepoUrl = GithubRepoUrlUtil.normalize(webhook.getRepoUrl());

        PullRequest pullRequest = pullRequestRepository
                .findByProjectAndPrNumber(project, webhook.getPrNumber())
                .orElseGet(PullRequest::new);

        pullRequest.setProject(project);
        pullRequest.setPrNumber(webhook.getPrNumber());
        pullRequest.setTitle(webhook.getPrTitle());
        pullRequest.setBranch(webhook.getBranch());
        pullRequest.setRepoUrl(normalizedRepoUrl);
        pullRequest.setGithubPrUrl(webhook.getPrHtmlUrl());
        pullRequest.setDiff(diff);
        if (pullRequest.getStatus() == null) {
            pullRequest.setStatus(PullRequest.ReviewStatus.PENDING);
        }
        return pullRequestRepository.save(pullRequest);
    }

    @Override
    @Transactional
    public void saveValidationResult(PullRequest pullRequest, AIResponseDTO aiResponse) {
        pullRequest.setScore(aiResponse.getScore());
        pullRequest.setAiSummary(aiResponse.getSummary());
        pullRequest.setStatus(aiResponse.getStatus());
        pullRequest.setMissingRequirementsJson(writeJson(aiResponse.getMissingRequirements()));
        pullRequest.setInlineCommentsJson(writeJson(aiResponse.getInlineComments()));
        pullRequestRepository.save(pullRequest);
    }

    @Override
    public List<PullRequestResponseDTO> getProjectPullRequests(String ownerEmail, Long projectId) {
        Users owner = getUser(ownerEmail);
        Project project = getOwnedProject(owner, projectId);
        return pullRequestRepository.findByProjectIdOrderByPrNumberDesc(project.getId()).stream()
                .map(prMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<PullRequestResponseDTO> getAllPullRequests(String ownerEmail) {
        Users owner = getUser(ownerEmail);
        return pullRequestRepository.findByProjectOwnerIdOrderByPrNumberDesc(owner.getId()).stream()
                .map(prMapper::toResponseDto)
                .toList();
    }

    @Override
    public PullRequestResponseDTO getPullRequest(String ownerEmail, Long pullRequestId) {
        Users owner = getUser(ownerEmail);
        PullRequest pullRequest = pullRequestRepository.findById(pullRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Pull request not found"));
        if (!pullRequest.getProject().getOwner().getId().equals(owner.getId())) {
            throw new ResourceNotFoundException("Pull request not found");
        }
        return prMapper.toResponseDto(pullRequest);
    }

    private Users getUser(String ownerEmail) {
        return usersRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Project getOwnedProject(Users owner, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        if (!project.getOwner().getId().equals(owner.getId())) {
            throw new ResourceNotFoundException("Project not found");
        }
        return project;
    }

    private String writeJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to serialize PR validation result", ex);
        }
    }
}
