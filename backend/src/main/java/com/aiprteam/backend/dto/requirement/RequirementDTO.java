package com.aiprteam.backend.dto.requirement;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequirementDTO {
    private Long id;
    private Long sprintId;

    @NotBlank
    private String title;

    @NotBlank
    private String description;
}
