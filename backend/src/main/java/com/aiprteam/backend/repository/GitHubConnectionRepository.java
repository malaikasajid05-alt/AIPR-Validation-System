package com.aiprteam.backend.repository;

import com.aiprteam.backend.entity.GitHubConnection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GitHubConnectionRepository extends JpaRepository<GitHubConnection, Long> {
    Optional<GitHubConnection> findByRepoFullName(String repoFullName);

    Optional<GitHubConnection> findFirstByRepoFullNameIgnoreCase(String repoFullName);

    List<GitHubConnection> findAllByRepoFullNameIgnoreCase(String repoFullName);

    Optional<GitHubConnection> findByRepoUrl(String repoUrl);
}
