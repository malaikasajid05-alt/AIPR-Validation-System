package com.aiprteam.backend.repository;

import com.aiprteam.backend.entity.Requirement;
import com.aiprteam.backend.entity.Sprint;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {
    List<Requirement> findBySprint(Sprint sprint);

    List<Requirement> findBySprintId(Long sprintId);
}
