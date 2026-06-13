package com.aiprteam.backend.config;

import com.aiprteam.backend.entity.Project;
import com.aiprteam.backend.repository.ProjectRepository;
import com.aiprteam.backend.repository.PullRequestRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Removes duplicate projects that share the same GitHub repository URL (keeps newest by id).
 * Runs once on startup in non-test profiles.
 */
@Component
@Order(1)
public class DataCleanupRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataCleanupRunner.class);

    private final ProjectRepository projectRepository;
    private final PullRequestRepository pullRequestRepository;

    public DataCleanupRunner(ProjectRepository projectRepository, PullRequestRepository pullRequestRepository) {
        this.projectRepository = projectRepository;
        this.pullRequestRepository = pullRequestRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        List<Project> allProjects = projectRepository.findAll();
        Map<String, List<Project>> grouped = allProjects.stream()
                .collect(Collectors.groupingBy(p -> p.getGithubRepoUrl().toLowerCase()));

        int removed = 0;
        for (Map.Entry<String, List<Project>> entry : grouped.entrySet()) {
            List<Project> duplicates = entry.getValue();
            if (duplicates.size() <= 1) {
                continue;
            }
            Project keep = duplicates.stream().max(Comparator.comparing(Project::getId)).orElseThrow();
            for (Project project : duplicates) {
                if (!project.getId().equals(keep.getId())) {
                    log.warn(
                            "Removing duplicate project id={} for repository {} (keeping id={})",
                            project.getId(),
                            entry.getKey(),
                            keep.getId());
                    pullRequestRepository
                            .findByProjectIdOrderByPrNumberDesc(project.getId())
                            .forEach(pullRequestRepository::delete);
                    projectRepository.delete(project);
                    removed++;
                }
            }
        }
        if (removed > 0) {
            log.info("Removed {} duplicate project(s) on startup.", removed);
        }
    }
}
