package com.aiprteam.backend.mapper;

import com.aiprteam.backend.dto.project.ProjectDTO;
import com.aiprteam.backend.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectDTO toDto(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setGithubRepoUrl(project.getGithubRepoUrl());
        return dto;
    }

    public Project toEntity(ProjectDTO dto) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setGithubRepoUrl(dto.getGithubRepoUrl());
        return project;
    }
}
