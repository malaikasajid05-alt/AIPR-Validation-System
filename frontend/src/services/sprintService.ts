import type { CreateSprintPayload, Sprint } from '@/types/sprint.types';
import { api } from './api';

export async function fetchSprints(projectId: number) {
  const { data } = await api.get<Sprint[]>(`/projects/${projectId}/sprints`);
  return data;
}

export async function createSprint(projectId: number, payload: CreateSprintPayload) {
  const { data } = await api.post<Sprint>(`/projects/${projectId}/sprints`, payload);
  return data;
}
