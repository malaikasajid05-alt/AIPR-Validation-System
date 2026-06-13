package com.aiprteam.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.github")
@Getter
@Setter
public class GithubConfig {
    private String webhookSecret;
    private String apiToken;
    private String apiBaseUrl;
}
