package com.aiprteam.backend.service.impl;

import com.aiprteam.backend.dto.project.ProjectDTO;
import com.aiprteam.backend.entity.GitHubConnection;
import com.aiprteam.backend.entity.Project;
import com.aiprteam.backend.entity.Users;
import com.aiprteam.backend.exception.GithubException;
import com.aiprteam.backend.exception.ResourceNotFoundException;
import com.aiprteam.backend.mapper.ProjectMapper;
import com.aiprteam.backend.repository.GitHubConnectionRepository;
import com.aiprteam.backend.repository.ProjectRepository;
import com.aiprteam.backend.repository.UsersRepository;
import com.aiprteam.backend.service.GithubService;
import com.aiprteam.backend.service.ProjectService;
import com.aiprteam.backend.util.GithubRepoUrlUtil;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final GitHubConnectionRepository gitHubConnectionRepository;
    private final UsersRepository usersRepository;
    private final ProjectMapper projectMapper;
    private final GithubService githubService;

    public ProjectServiceImpl(
            ProjectRepository projectRepository,
            GitHubConnectionRepository gitHubConnectionRepository,
            UsersRepository usersRepository,
            ProjectMapper projectMapper,
            GithubService githubService) {
        this.projectRepository = projectRepository;
        this.gitHubConnectionRepository = gitHubConnectionRepository;
        this.usersRepository = usersRepository;
        this.projectMapper = projectMapper;
        this.githubService = githubService;
    }

    @Override
    @Transactional
    public ProjectDTO createProject(String ownerEmail, ProjectDTO request) {
        Users owner = usersRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String normalizedRepoUrl = GithubRepoUrlUtil.normalize(request.getGithubRepoUrl());
        String repoFullName = GithubRepoUrlUtil.extractFullName(normalizedRepoUrl);

        if (gitHubConnectionRepository.findFirstByRepoFullNameIgnoreCase(repoFullName).isPresent()
                || !projectRepository.findAllByGithubRepoUrlIgnoreCase(normalizedRepoUrl).isEmpty()) {
            throw new GithubException("This repository is already linked to a project.");
        }

        if (!githubService.validateRepositoryUrl(normalizedRepoUrl)) {
            throw new GithubException("GitHub repository URL is invalid or inaccessible");
        }

        request.setGithubRepoUrl(normalizedRepoUrl);
        Project project = projectMapper.toEntity(request);
        project.setOwner(owner);

        GitHubConnection connection = new GitHubConnection();
        connection.setProject(project);
        connection.setRepoUrl(normalizedRepoUrl);
        connection.setRepoFullName(repoFullName);
        project.setGithubConnection(connection);

        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    public List<ProjectDTO> getProjects(String ownerEmail) {
        Users owner = usersRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return projectRepository.findByOwner(owner).stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Override
    public ProjectDTO getProject(String ownerEmail, Long projectId) {
        Users owner = usersRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        if (!project.getOwner().getId().equals(owner.getId())) {
            throw new ResourceNotFoundException("Project not found");
        }
        return projectMapper.toDto(project);
    }
}
