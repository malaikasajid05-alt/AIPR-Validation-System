export interface Requirement {
  id: number;
  sprintId: number;
  title: string;
  description: string;
}

export interface CreateRequirementPayload {
  title: string;
  description: string;
}
