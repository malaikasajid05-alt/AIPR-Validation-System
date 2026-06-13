package com.aiprteam.backend.service;

import com.aiprteam.backend.dto.project.ProjectDTO;
import java.util.List;

public interface ProjectService {
    ProjectDTO createProject(String ownerEmail, ProjectDTO request);

    List<ProjectDTO> getProjects(String ownerEmail);

    ProjectDTO getProject(String ownerEmail, Long projectId);
}
