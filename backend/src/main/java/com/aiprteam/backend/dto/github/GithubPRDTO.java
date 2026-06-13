package com.aiprteam.backend.dto.github;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubPRDTO {
    private Integer number;
    private String title;
    private String branch;
    private String repoFullName;
    private String repoUrl;
    private String diffUrl;
    private String htmlUrl;
    private String diff;
}
