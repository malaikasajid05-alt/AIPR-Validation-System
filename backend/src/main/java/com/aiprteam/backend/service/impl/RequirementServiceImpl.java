package com.aiprteam.backend.service.impl;

import com.aiprteam.backend.dto.requirement.RequirementDTO;
import com.aiprteam.backend.entity.Requirement;
import com.aiprteam.backend.entity.Sprint;
import com.aiprteam.backend.entity.Users;
import com.aiprteam.backend.exception.ResourceNotFoundException;
import com.aiprteam.backend.mapper.RequirementMapper;
import com.aiprteam.backend.repository.ProjectRepository;
import com.aiprteam.backend.repository.RequirementRepository;
import com.aiprteam.backend.repository.SprintRepository;
import com.aiprteam.backend.repository.UsersRepository;
import com.aiprteam.backend.service.RequirementService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequirementServiceImpl implements RequirementService {

    private final RequirementRepository requirementRepository;
    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;
    private final UsersRepository usersRepository;
    private final RequirementMapper requirementMapper;

    public RequirementServiceImpl(
            RequirementRepository requirementRepository,
            SprintRepository sprintRepository,
            ProjectRepository projectRepository,
            UsersRepository usersRepository,
            RequirementMapper requirementMapper) {
        this.requirementRepository = requirementRepository;
        this.sprintRepository = sprintRepository;
        this.projectRepository = projectRepository;
        this.usersRepository = usersRepository;
        this.requirementMapper = requirementMapper;
    }

    @Override
    @Transactional
    public RequirementDTO createRequirement(String ownerEmail, Long sprintId, RequirementDTO request) {
        Sprint sprint = getOwnedSprint(ownerEmail, sprintId);
        Requirement requirement = requirementMapper.toEntity(request);
        requirement.setSprint(sprint);
        return requirementMapper.toDto(requirementRepository.save(requirement));
    }

    @Override
    public List<RequirementDTO> getRequirements(String ownerEmail, Long sprintId) {
        Sprint sprint = getOwnedSprint(ownerEmail, sprintId);
        return requirementRepository.findBySprint(sprint).stream()
                .map(requirementMapper::toDto)
                .toList();
    }

    private Sprint getOwnedSprint(String ownerEmail, Long sprintId) {
        Users owner = usersRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint not found"));
        if (!sprint.getProject().getOwner().getId().equals(owner.getId())) {
            throw new ResourceNotFoundException("Sprint not found");
        }
        return sprint;
    }
}
