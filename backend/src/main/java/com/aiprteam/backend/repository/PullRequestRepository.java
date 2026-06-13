package com.aiprteam.backend.repository;

import com.aiprteam.backend.entity.Project;
import com.aiprteam.backend.entity.PullRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {
    Optional<PullRequest> findByProjectAndPrNumber(Project project, Integer prNumber);

    List<PullRequest> findByProjectIdOrderByPrNumberDesc(Long projectId);

    List<PullRequest> findByProjectOwnerIdOrderByPrNumberDesc(Long ownerId);
}
