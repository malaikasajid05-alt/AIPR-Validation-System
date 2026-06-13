package com.aiprteam.backend.controller;

import com.aiprteam.backend.dto.project.ProjectDTO;
import com.aiprteam.backend.service.ProjectService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ProjectDTO createProject(
            @AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody ProjectDTO request) {
        return projectService.createProject(userDetails.getUsername(), request);
    }

    @GetMapping
    public List<ProjectDTO> getProjects(@AuthenticationPrincipal UserDetails userDetails) {
        return projectService.getProjects(userDetails.getUsername());
    }

    @GetMapping("/{projectId}")
    public ProjectDTO getProject(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long projectId) {
        return projectService.getProject(userDetails.getUsername(), projectId);
    }
}
