export interface Project {
  id: number;
  name: string;
  description?: string;
  githubRepoUrl: string;
}

export interface CreateProjectPayload {
  name: string;
  description?: string;
  githubRepoUrl: string;
}
