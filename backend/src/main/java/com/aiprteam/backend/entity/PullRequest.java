package com.aiprteam.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "pull_requests",
        uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "pr_number"}))
@Getter
@Setter
@NoArgsConstructor
public class PullRequest {

    public enum ReviewStatus {
        PENDING,
        APPROVED,
        REJECTED,
        NEEDS_CHANGES
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private Integer prNumber;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private String repoUrl;

    @Column(length = 4000)
    private String githubPrUrl;

    @Column(length = 100000)
    private String diff;

    private Integer score;

    @Column(length = 4000)
    private String aiSummary;

    @Enumerated(EnumType.STRING)
  private ReviewStatus status = ReviewStatus.PENDING;

    @Column(length = 50000)
    private String inlineCommentsJson;

    @Column(length = 5000)
    private String missingRequirementsJson;
}
