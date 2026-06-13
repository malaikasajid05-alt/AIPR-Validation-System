package com.aiprteam.backend.dto.github;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubCommentDTO {
    private String body;
    private String path;
    private Integer line;
    private String commitId;
}
