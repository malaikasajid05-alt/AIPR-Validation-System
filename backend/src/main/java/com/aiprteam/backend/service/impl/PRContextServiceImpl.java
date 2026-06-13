package com.aiprteam.backend.service.impl;

import com.aiprteam.backend.dto.ai.AIContextDTO;
import com.aiprteam.backend.entity.PullRequest;
import com.aiprteam.backend.entity.Requirement;
import com.aiprteam.backend.entity.Sprint;
import com.aiprteam.backend.repository.RequirementRepository;
import com.aiprteam.backend.repository.SprintRepository;
import com.aiprteam.backend.service.PRContextService;
import com.aiprteam.backend.util.GithubDiffParserUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PRContextServiceImpl implements PRContextService {

    private final SprintRepository sprintRepository;
    private final RequirementRepository requirementRepository;
    private final GithubDiffParserUtil githubDiffParserUtil;

    public PRContextServiceImpl(
            SprintRepository sprintRepository,
            RequirementRepository requirementRepository,
            GithubDiffParserUtil githubDiffParserUtil) {
        this.sprintRepository = sprintRepository;
        this.requirementRepository = requirementRepository;
        this.githubDiffParserUtil = githubDiffParserUtil;
    }

    @Override
    public AIContextDTO buildContext(PullRequest pullRequest, String diff) {
        AIContextDTO context = new AIContextDTO();
        context.setPrDiff(diff);
        context.setRequirements(loadRequirements(pullRequest.getProject().getId()));
        context.setProjectMeta(buildProjectMeta(pullRequest));
        context.setFileChanges(githubDiffParserUtil.extractChangedFiles(diff));
        context.setPreviousReview(buildPreviousReview(pullRequest));
        return context;
    }

    private List<String> loadRequirements(Long projectId) {
        List<Sprint> sprints = sprintRepository.findByProjectId(projectId);
        return sprints.stream()
                .flatMap(sprint -> requirementRepository.findBySprint(sprint).stream())
                .map(this::formatRequirement)
                .collect(Collectors.toList());
    }

    private String formatRequirement(Requirement requirement) {
        return requirement.getTitle() + ": " + requirement.getDescription();
    }

    private Map<String, Object> buildProjectMeta(PullRequest pullRequest) {
        Map<String, Object> meta = new HashMap<>();
        meta.put("projectId", pullRequest.getProject().getId());
        meta.put("projectName", pullRequest.getProject().getName());
        meta.put("repoUrl", pullRequest.getRepoUrl());
        meta.put("prNumber", pullRequest.getPrNumber());
        meta.put("branch", pullRequest.getBranch());
        return meta;
    }

    private Map<String, Object> buildPreviousReview(PullRequest pullRequest) {
        Map<String, Object> previous = new HashMap<>();
        if (pullRequest.getScore() != null) {
            previous.put("score", pullRequest.getScore());
        }
        if (pullRequest.getAiSummary() != null) {
            previous.put("summary", pullRequest.getAiSummary());
        }
        if (pullRequest.getStatus() != null) {
            previous.put("status", pullRequest.getStatus().name());
        }
        return previous;
    }
}
