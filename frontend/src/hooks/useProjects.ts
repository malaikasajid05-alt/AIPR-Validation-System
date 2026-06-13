import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { createProject, fetchProject, fetchProjects } from '@/services/projectService';
import type { CreateProjectPayload } from '@/types/project.types';

export function useProjects() {
  return useQuery({
    queryKey: ['projects'],
    queryFn: fetchProjects,
  });
}

export function useProject(projectId: number) {
  return useQuery({
    queryKey: ['projects', projectId],
    queryFn: () => fetchProject(projectId),
    enabled: Number.isFinite(projectId),
  });
}

export function useCreateProject() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (payload: CreateProjectPayload) => createProject(payload),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['projects'] });
    },
  });
}
