package com.aiprteam.backend.dto.ai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AISummaryDTO {
    private Integer score;
    private String summary;
    private String status;
}
