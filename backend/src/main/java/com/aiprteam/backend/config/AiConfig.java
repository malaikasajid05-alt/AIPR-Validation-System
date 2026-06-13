package com.aiprteam.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.ai")
@Getter
@Setter
public class AiConfig {
    private String apiUrl;
    private String apiKey;
    private String model;
    private int approvalThreshold;
}
