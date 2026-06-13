package com.aiprteam.backend.dto.ai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InlineCommentDTO {
    private String file;
    private Integer line;
    private String comment;
    private String severity;
}
