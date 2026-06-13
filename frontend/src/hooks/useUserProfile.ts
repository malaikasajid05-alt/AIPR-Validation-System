import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { fetchCurrentUserProfile, updateUserPassword, updateUserProfile } from '@/services/userService';

export function useUserProfile() {
  return useQuery({
    queryKey: ['user-profile'],
    queryFn: fetchCurrentUserProfile,
  });
}

export function useUpdateUserProfile() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: updateUserProfile,
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['user-profile'] });
    },
  });
}

export function useUpdateUserPassword() {
  return useMutation({
    mutationFn: updateUserPassword,
  });
}
