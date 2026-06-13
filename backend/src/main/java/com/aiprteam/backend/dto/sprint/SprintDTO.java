package com.aiprteam.backend.dto.sprint;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SprintDTO {
    private Long id;
    private Long projectId;

    @NotBlank
    private String name;

    private String description;
}
