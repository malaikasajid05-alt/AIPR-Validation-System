package com.aiprteam.backend.util;

import com.aiprteam.backend.entity.PullRequest;
import org.springframework.stereotype.Component;

@Component
public class ScoreCalculatorUtil {

    public PullRequest.ReviewStatus deriveStatus(int score, int approvalThreshold) {
        if (score >= approvalThreshold) {
            return PullRequest.ReviewStatus.APPROVED;
        }
        if (score >= approvalThreshold - 20) {
            return PullRequest.ReviewStatus.NEEDS_CHANGES;
        }
        return PullRequest.ReviewStatus.REJECTED;
    }
}
