package com.aiprteam.backend.mapper;

import com.aiprteam.backend.dto.requirement.RequirementDTO;
import com.aiprteam.backend.entity.Requirement;
import org.springframework.stereotype.Component;

@Component
public class RequirementMapper {

    public RequirementDTO toDto(Requirement requirement) {
        RequirementDTO dto = new RequirementDTO();
        dto.setId(requirement.getId());
        dto.setSprintId(requirement.getSprint().getId());
        dto.setTitle(requirement.getTitle());
        dto.setDescription(requirement.getDescription());
        return dto;
    }

    public Requirement toEntity(RequirementDTO dto) {
        Requirement requirement = new Requirement();
        requirement.setTitle(dto.getTitle());
        requirement.setDescription(dto.getDescription());
        return requirement;
    }
}
