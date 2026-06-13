import { api } from './api';

export interface UserProfile {
  id: number;
  email: string;
  fullName: string;
}

export async function fetchCurrentUserProfile() {
  const { data } = await api.get<UserProfile>('/users/me');
  return data;
}

export async function updateUserProfile(payload: { fullName: string }) {
  const { data } = await api.put<UserProfile>('/users/me', payload);
  return data;
}

export async function updateUserPassword(payload: { currentPassword: string; newPassword: string }) {
  await api.put('/users/me/password', payload);
}
