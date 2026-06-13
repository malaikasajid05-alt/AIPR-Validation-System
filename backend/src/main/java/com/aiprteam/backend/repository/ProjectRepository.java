package com.aiprteam.backend.repository;

import com.aiprteam.backend.entity.Project;
import com.aiprteam.backend.entity.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwner(Users owner);

    Optional<Project> findByGithubRepoUrl(String githubRepoUrl);

    List<Project> findAllByGithubRepoUrlIgnoreCase(String githubRepoUrl);
}
