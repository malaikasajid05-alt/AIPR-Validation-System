package com.aiprteam.backend.dto.pr;

import com.aiprteam.backend.dto.ai.InlineCommentDTO;
import com.aiprteam.backend.entity.PullRequest;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PullRequestDTO {
    private Long id;
    private Long projectId;
    private Integer prNumber;
    private String title;
    private String branch;
    private String repoUrl;
    private String githubPrUrl;
    private Integer score;
    private String aiSummary;
    private PullRequest.ReviewStatus status;
}
