package com.aiprteam.backend.service.impl;

import com.aiprteam.backend.config.AiConfig;
import com.aiprteam.backend.dto.ai.AIContextDTO;
import com.aiprteam.backend.dto.ai.AIResponseDTO;
import com.aiprteam.backend.entity.PullRequest;
import com.aiprteam.backend.service.AIClientService;
import com.aiprteam.backend.service.AIValidationService;
import com.aiprteam.backend.service.PRContextService;
import com.aiprteam.backend.util.AIResponseParserUtil;
import com.aiprteam.backend.util.PromptBuilderUtil;
import org.springframework.stereotype.Service;

@Service
public class AIValidationServiceImpl implements AIValidationService {

    private final PRContextService prContextService;
    private final PromptBuilderUtil promptBuilderUtil;
    private final AIClientService aiClientService;
    private final AIResponseParserUtil aiResponseParserUtil;
    private final AiConfig aiConfig;

    public AIValidationServiceImpl(
            PRContextService prContextService,
            PromptBuilderUtil promptBuilderUtil,
            AIClientService aiClientService,
            AIResponseParserUtil aiResponseParserUtil,
            AiConfig aiConfig) {
        this.prContextService = prContextService;
        this.promptBuilderUtil = promptBuilderUtil;
        this.aiClientService = aiClientService;
        this.aiResponseParserUtil = aiResponseParserUtil;
        this.aiConfig = aiConfig;
    }

    @Override
    public AIResponseDTO validate(PullRequest pullRequest, String diff) {
        AIContextDTO context = prContextService.buildContext(pullRequest, diff);
        String prompt = promptBuilderUtil.buildValidationPrompt(context);
        String rawResponse = aiClientService.callAi(prompt);
        AIResponseDTO parsed = aiResponseParserUtil.parse(rawResponse);
        if (parsed.getScore() != null
                && parsed.getScore() < aiConfig.getApprovalThreshold()
                && parsed.getStatus() == PullRequest.ReviewStatus.APPROVED) {
            parsed.setStatus(PullRequest.ReviewStatus.NEEDS_CHANGES);
        }
        return parsed;
    }
}
