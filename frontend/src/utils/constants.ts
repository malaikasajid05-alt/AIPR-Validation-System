export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export const ROUTES = {
  login: '/login',
  register: '/register',
  dashboard: '/dashboard',
  projects: '/projects',
  prReviews: '/pr-reviews',
  webhooks: '/webhooks',
  settings: '/settings',
} as const;

export const WEBHOOK_STORAGE_KEY = 'aipr-webhook-keys';
