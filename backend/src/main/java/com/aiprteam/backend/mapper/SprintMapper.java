package com.aiprteam.backend.mapper;

import com.aiprteam.backend.dto.sprint.SprintDTO;
import com.aiprteam.backend.entity.Sprint;
import org.springframework.stereotype.Component;

@Component
public class SprintMapper {

    public SprintDTO toDto(Sprint sprint) {
        SprintDTO dto = new SprintDTO();
        dto.setId(sprint.getId());
        dto.setProjectId(sprint.getProject().getId());
        dto.setName(sprint.getName());
        dto.setDescription(sprint.getDescription());
        return dto;
    }

    public Sprint toEntity(SprintDTO dto) {
        Sprint sprint = new Sprint();
        sprint.setName(dto.getName());
        sprint.setDescription(dto.getDescription());
        return sprint;
    }
}
