package com.aiprteam.backend.service;

import com.aiprteam.backend.dto.requirement.RequirementDTO;
import java.util.List;

public interface RequirementService {
    RequirementDTO createRequirement(String ownerEmail, Long sprintId, RequirementDTO request);

    List<RequirementDTO> getRequirements(String ownerEmail, Long sprintId);
}
