package com.aiprteam.backend.dto.github;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubWebhookDTO {
    private String action;
    private String repoUrl;
    private String repoFullName;
    private Integer prNumber;
    private String branch;
    private String diffUrl;
    private String prTitle;
    private String prHtmlUrl;
}
