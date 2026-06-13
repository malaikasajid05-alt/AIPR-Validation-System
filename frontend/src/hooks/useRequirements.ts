import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { createRequirement, fetchRequirements } from '@/services/requirementService';
import type { CreateRequirementPayload } from '@/types/requirement.types';

export function useRequirements(sprintId: number) {
  return useQuery({
    queryKey: ['sprints', sprintId, 'requirements'],
    queryFn: () => fetchRequirements(sprintId),
    enabled: Number.isFinite(sprintId),
  });
}

export function useCreateRequirement(sprintId: number) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (payload: CreateRequirementPayload) => createRequirement(sprintId, payload),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['sprints', sprintId, 'requirements'] });
    },
  });
}
