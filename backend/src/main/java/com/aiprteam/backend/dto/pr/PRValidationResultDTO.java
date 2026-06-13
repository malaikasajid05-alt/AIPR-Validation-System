package com.aiprteam.backend.dto.pr;

import com.aiprteam.backend.dto.ai.InlineCommentDTO;
import com.aiprteam.backend.entity.PullRequest;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PRValidationResultDTO {
    private Integer score;
    private String summary;
    private PullRequest.ReviewStatus status;
    private List<String> missingRequirements;
    private List<InlineCommentDTO> inlineComments;
}
