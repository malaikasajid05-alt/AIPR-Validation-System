import type { PullRequestReview } from '@/types/pr.types';
import { api } from './api';

export async function fetchPullRequests() {
  const { data } = await api.get<PullRequestReview[]>('/pull-requests');
  return data;
}

export async function fetchProjectPullRequests(projectId: number) {
  const { data } = await api.get<PullRequestReview[]>(`/projects/${projectId}/pull-requests`);
  return data;
}

export async function fetchPullRequest(pullRequestId: number) {
  const { data } = await api.get<PullRequestReview>(`/pull-requests/${pullRequestId}`);
  return data;
}
