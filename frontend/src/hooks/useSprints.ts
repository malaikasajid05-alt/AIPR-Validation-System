import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { createSprint, fetchSprints } from '@/services/sprintService';
import type { CreateSprintPayload } from '@/types/sprint.types';

export function useSprints(projectId: number) {
  return useQuery({
    queryKey: ['projects', projectId, 'sprints'],
    queryFn: () => fetchSprints(projectId),
    enabled: Number.isFinite(projectId),
  });
}

export function useCreateSprint(projectId: number) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (payload: CreateSprintPayload) => createSprint(projectId, payload),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['projects', projectId, 'sprints'] });
    },
  });
}
