export type ReviewStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'NEEDS_CHANGES';

export interface InlineComment {
  file: string;
  line: number;
  comment: string;
  severity: 'INFO' | 'WARN' | 'ERROR' | string;
}

export interface PullRequestReview {
  id: number;
  projectId: number;
  projectName?: string;
  prNumber: number;
  title: string;
  branch: string;
  repoUrl: string;
  githubPrUrl?: string;
  score?: number;
  aiSummary?: string;
  status: ReviewStatus;
  missingRequirements?: string[];
  inlineComments?: InlineComment[];
}
