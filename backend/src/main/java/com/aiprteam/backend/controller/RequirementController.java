package com.aiprteam.backend.controller;

import com.aiprteam.backend.dto.requirement.RequirementDTO;
import com.aiprteam.backend.service.RequirementService;
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
@RequestMapping("/api/sprints/{sprintId}/requirements")
public class RequirementController {

    private final RequirementService requirementService;

    public RequirementController(RequirementService requirementService) {
        this.requirementService = requirementService;
    }

    @PostMapping
    public RequirementDTO createRequirement(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long sprintId,
            @Valid @RequestBody RequirementDTO request) {
        return requirementService.createRequirement(userDetails.getUsername(), sprintId, request);
    }

    @GetMapping
    public List<RequirementDTO> getRequirements(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long sprintId) {
        return requirementService.getRequirements(userDetails.getUsername(), sprintId);
    }
}
