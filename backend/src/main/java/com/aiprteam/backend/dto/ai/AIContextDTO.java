package com.aiprteam.backend.dto.ai;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIContextDTO {
    private String prDiff;
    private List<String> requirements;
    private Map<String, Object> projectMeta;
    private List<String> fileChanges;
    private Map<String, Object> previousReview;
}
