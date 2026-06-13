import { useAuthStore } from '@/store/authStore';

export function useAuth() {
  const token = useAuthStore((state) => state.token);
  const user = useAuthStore((state) => state.user);
  const setAuth = useAuthStore((state) => state.setAuth);
  const updateUser = useAuthStore((state) => state.updateUser);
  const clearAuth = useAuthStore((state) => state.clearAuth);
  const hydrateFromStorage = useAuthStore((state) => state.hydrateFromStorage);

  return {
    token,
    user,
    isAuthenticated: Boolean(token || localStorage.getItem('token')),
    setAuth,
    updateUser,
    clearAuth,
    hydrateFromStorage,
  };
}
