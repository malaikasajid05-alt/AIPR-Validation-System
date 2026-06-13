import type { ApiErrorBody } from '@/types/api.types';
import type { AuthResponse, LoginPayload, RegisterPayload } from '@/types/auth.types';
import { api } from './api';

export async function loginRequest(payload: LoginPayload) {
  const { data } = await api.post<AuthResponse>('/auth/login', payload);
  return data;
}

export async function registerRequest(payload: RegisterPayload) {
  const { data } = await api.post<AuthResponse>('/auth/register', payload);
  return data;
}

export function getApiErrorMessage(error: unknown, fallback: string) {
  if (typeof error === 'object' && error !== null && 'response' in error) {
    const response = (error as { response?: { data?: ApiErrorBody; status?: number } }).response;
    const message = response?.data?.message;
    if (response?.status === 401) {
      return 'Incorrect email or password.';
    }
    if (message) {
      const lower = message.toLowerCase();
      if (
        lower.includes('invalid github repository url') ||
        lower.includes('github repository url is invalid') ||
        lower.includes('only github.com')
      ) {
        return 'That repository URL could not be validated. Use https://github.com/owner/repo (optionally with /tree/... or .git).';
      }
      if (lower.includes('bad credentials')) {
        return 'Incorrect password.';
      }
      return message;
    }
  }
  if (error instanceof Error && error.message) {
    return error.message;
  }
  return fallback;
}
