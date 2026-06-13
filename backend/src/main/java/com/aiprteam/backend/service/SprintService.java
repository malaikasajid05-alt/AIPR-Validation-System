package com.aiprteam.backend.service;

import com.aiprteam.backend.dto.sprint.SprintDTO;
import java.util.List;

public interface SprintService {
    SprintDTO createSprint(String ownerEmail, Long projectId, SprintDTO request);

    List<SprintDTO> getSprints(String ownerEmail, Long projectId);
}
