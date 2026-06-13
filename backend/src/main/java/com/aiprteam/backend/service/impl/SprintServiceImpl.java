package com.aiprteam.backend.service.impl;

import com.aiprteam.backend.dto.sprint.SprintDTO;
import com.aiprteam.backend.entity.Project;
import com.aiprteam.backend.entity.Sprint;
import com.aiprteam.backend.entity.Users;
import com.aiprteam.backend.exception.ResourceNotFoundException;
import com.aiprteam.backend.mapper.SprintMapper;
import com.aiprteam.backend.repository.ProjectRepository;
import com.aiprteam.backend.repository.SprintRepository;
import com.aiprteam.backend.repository.UsersRepository;
import com.aiprteam.backend.service.SprintService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SprintServiceImpl implements SprintService {

    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;
    private final UsersRepository usersRepository;
    private final SprintMapper sprintMapper;

    public SprintServiceImpl(
            SprintRepository sprintRepository,
            ProjectRepository projectRepository,
            UsersRepository usersRepository,
            SprintMapper sprintMapper) {
        this.sprintRepository = sprintRepository;
        this.projectRepository = projectRepository;
        this.usersRepository = usersRepository;
        this.sprintMapper = sprintMapper;
    }

    @Override
    @Transactional
    public SprintDTO createSprint(String ownerEmail, Long projectId, SprintDTO request) {
        Project project = getOwnedProject(ownerEmail, projectId);
        Sprint sprint = sprintMapper.toEntity(request);
        sprint.setProject(project);
        return sprintMapper.toDto(sprintRepository.save(sprint));
    }

    @Override
    public List<SprintDTO> getSprints(String ownerEmail, Long projectId) {
        Project project = getOwnedProject(ownerEmail, projectId);
        return sprintRepository.findByProject(project).stream()
                .map(sprintMapper::toDto)
                .toList();
    }

    private Project getOwnedProject(String ownerEmail, Long projectId) {
        Users owner = usersRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        if (!project.getOwner().getId().equals(owner.getId())) {
            throw new ResourceNotFoundException("Project not found");
        }
        return project;
    }
}
