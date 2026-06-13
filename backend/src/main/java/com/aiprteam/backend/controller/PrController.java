package com.aiprteam.backend.controller;

import com.aiprteam.backend.dto.pr.PullRequestResponseDTO;
import com.aiprteam.backend.service.PrService;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PrController {

    private final PrService prService;

    public PrController(PrService prService) {
        this.prService = prService;
    }

    @GetMapping("/projects/{projectId}/pull-requests")
    public List<PullRequestResponseDTO> getProjectPullRequests(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long projectId) {
        return prService.getProjectPullRequests(userDetails.getUsername(), projectId);
    }

    @GetMapping("/pull-requests")
    public List<PullRequestResponseDTO> getAllPullRequests(@AuthenticationPrincipal UserDetails userDetails) {
        return prService.getAllPullRequests(userDetails.getUsername());
    }

    @GetMapping("/pull-requests/{pullRequestId}")
    public PullRequestResponseDTO getPullRequest(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long pullRequestId) {
        return prService.getPullRequest(userDetails.getUsername(), pullRequestId);
    }
}
