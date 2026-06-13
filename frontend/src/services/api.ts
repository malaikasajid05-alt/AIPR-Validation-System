import axios from 'axios';
import { API_BASE_URL, ROUTES } from '@/utils/constants';
import { useAuthStore } from '@/store/authStore';

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      useAuthStore.getState().clearAuth();
      if (!window.location.pathname.startsWith(ROUTES.login)) {
        window.location.href = ROUTES.login;
      }
    }
    return Promise.reject(error);
  },
);
