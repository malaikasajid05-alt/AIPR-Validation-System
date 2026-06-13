import type { CreateRequirementPayload, Requirement } from '@/types/requirement.types';
import { api } from './api';

export async function fetchRequirements(sprintId: number) {
  const { data } = await api.get<Requirement[]>(`/sprints/${sprintId}/requirements`);
  return data;
}

export async function createRequirement(sprintId: number, payload: CreateRequirementPayload) {
  const { data } = await api.post<Requirement>(`/sprints/${sprintId}/requirements`, payload);
  return data;
}
