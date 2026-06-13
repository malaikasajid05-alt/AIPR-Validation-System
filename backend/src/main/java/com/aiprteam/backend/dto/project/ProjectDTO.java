package com.aiprteam.backend.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDTO {
    private Long id;
    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String githubRepoUrl;
}
