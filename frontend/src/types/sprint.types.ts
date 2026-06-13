export interface Sprint {
  id: number;
  projectId: number;
  name: string;
  description?: string;
}

export interface CreateSprintPayload {
  name: string;
  description?: string;
}
