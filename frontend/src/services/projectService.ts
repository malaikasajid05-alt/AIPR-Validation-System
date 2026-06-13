import type { CreateProjectPayload, Project } from '@/types/project.types';
import { api } from './api';

export async function fetchProjects() {
  const { data } = await api.get<Project[]>('/projects');
  return data;
}

export async function fetchProject(projectId: number) {
  const { data } = await api.get<Project>(`/projects/${projectId}`);
  return data;
}

export async function createProject(payload: CreateProjectPayload) {
  const { data } = await api.post<Project>('/projects', payload);
  return data;
}
