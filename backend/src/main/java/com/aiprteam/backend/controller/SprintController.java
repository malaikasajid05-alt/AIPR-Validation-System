package com.aiprteam.backend.controller;

import com.aiprteam.backend.dto.sprint.SprintDTO;
import com.aiprteam.backend.service.SprintService;
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
@RequestMapping("/api/projects/{projectId}/sprints")
public class SprintController {

    private final SprintService sprintService;

    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @PostMapping
    public SprintDTO createSprint(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long projectId,
            @Valid @RequestBody SprintDTO request) {
        return sprintService.createSprint(userDetails.getUsername(), projectId, request);
    }

    @GetMapping
    public List<SprintDTO> getSprints(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long projectId) {
        return sprintService.getSprints(userDetails.getUsername(), projectId);
    }
}
