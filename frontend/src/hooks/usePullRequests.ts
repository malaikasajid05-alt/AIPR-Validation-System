import { useQuery } from '@tanstack/react-query';
import { fetchProjectPullRequests, fetchPullRequest, fetchPullRequests } from '@/services/prService';

export function usePullRequests() {
  return useQuery({
    queryKey: ['pull-requests'],
    queryFn: fetchPullRequests,
  });
}

export function useProjectPullRequests(projectId: number) {
  return useQuery({
    queryKey: ['projects', projectId, 'pull-requests'],
    queryFn: () => fetchProjectPullRequests(projectId),
    enabled: Number.isFinite(projectId),
  });
}

export function usePullRequest(pullRequestId: number) {
  return useQuery({
    queryKey: ['pull-requests', pullRequestId],
    queryFn: () => fetchPullRequest(pullRequestId),
    enabled: Number.isFinite(pullRequestId),
  });
}
