import { useEffect, useState, type FormEvent } from 'react';
import { Button } from '@/components/common/Button';
import { Card } from '@/components/common/Card';
import { Input } from '@/components/common/Input';
import { Loader } from '@/components/common/Loader';
import { useTheme } from '@/context/ThemeContext';
import { useAuth } from '@/hooks/useAuth';
import { useUpdateUserPassword, useUpdateUserProfile, useUserProfile } from '@/hooks/useUserProfile';
import { getApiErrorMessage } from '@/services/authService';
import { useToastStore } from '@/store/toastStore';
import { validateConfirmPassword, validatePassword, validateRequired } from '@/utils/validators';

export function SettingsPage() {
  const { user, updateUser } = useAuth();
  const { theme, toggleTheme } = useTheme();
  const pushToast = useToastStore((state) => state.pushToast);
  const profileQuery = useUserProfile();
  const updateProfile = useUpdateUserProfile();
  const updatePassword = useUpdateUserPassword();

  const [displayName, setDisplayName] = useState('');
  const [profileErrors, setProfileErrors] = useState({ displayName: '', general: '' });

  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [passwordErrors, setPasswordErrors] = useState({
    current: '',
    newPassword: '',
    confirm: '',
    general: '',
  });

  useEffect(() => {
    if (profileQuery.data) {
      setDisplayName(profileQuery.data.fullName);
    } else if (user?.fullName) {
      setDisplayName(user.fullName);
    }
  }, [profileQuery.data, user?.fullName]);

  const handleProfileSubmit = async (event: FormEvent) => {
    event.preventDefault();
    const nameError = validateRequired(displayName, 'Display name');
    setProfileErrors({ displayName: nameError, general: '' });
    if (nameError) {
      return;
    }
    try {
      const updated = await updateProfile.mutateAsync({ fullName: displayName.trim() });
      updateUser({ fullName: updated.fullName });
      pushToast('Display name updated.', 'success');
    } catch (error) {
      const message = getApiErrorMessage(error, 'Unable to update profile.');
      setProfileErrors((current) => ({ ...current, general: message }));
      pushToast(message, 'error');
    }
  };

  const handlePasswordSubmit = async (event: FormEvent) => {
    event.preventDefault();
    const nextErrors = {
      current: validateRequired(currentPassword, 'Current password'),
      newPassword: validatePassword(newPassword),
      confirm: validateConfirmPassword(newPassword, confirmPassword),
      general: '',
    };
    setPasswordErrors(nextErrors);
    if (nextErrors.current || nextErrors.newPassword || nextErrors.confirm) {
      return;
    }
    try {
      await updatePassword.mutateAsync({
        currentPassword,
        newPassword,
      });
      setCurrentPassword('');
      setNewPassword('');
      setConfirmPassword('');
      pushToast('Password updated.', 'success');
    } catch (error) {
      const message = getApiErrorMessage(error, 'Unable to update password.');
      setPasswordErrors((current) => ({ ...current, general: message }));
      pushToast(message, 'error');
    }
  };

  if (profileQuery.isLoading) {
    return <Loader />;
  }

  return (
    <div className="page-shell">
      <div>
        <p className="font-headline text-sm font-semibold uppercase tracking-[0.18em] text-primary">Settings</p>
        <h1 className="mt-2 font-headline text-4xl font-extrabold text-text-primary">Account & preferences</h1>
        <p className="mt-3 max-w-2xl font-body text-lg text-slate-600 dark:text-slate-300">
          Manage your profile, security, and appearance. Your email ({user?.email}) is used for sign-in and cannot be
          changed here.
        </p>
      </div>

      <Card>
        <h2 className="font-headline text-2xl font-bold">Appearance</h2>
        <p className="mt-2 font-body text-slate-600 dark:text-slate-300">Switch between light and dark interface themes.</p>
        <div className="mt-6 flex flex-wrap items-center gap-4">
          <span className="font-headline text-sm font-semibold capitalize">{theme} mode</span>
          <Button type="button" variant="secondary" onClick={toggleTheme}>
            Toggle {theme === 'dark' ? 'light' : 'dark'} theme
          </Button>
        </div>
      </Card>

      <Card>
        <h2 className="font-headline text-2xl font-bold">Profile</h2>
        <p className="mt-2 font-body text-slate-600 dark:text-slate-300">Update the display name shown across the app.</p>
        <form className="mt-6 space-y-4" onSubmit={handleProfileSubmit}>
          <Input
            label="Display name"
            value={displayName}
            onChange={(event) => setDisplayName(event.target.value)}
            error={profileErrors.displayName}
          />
          {profileErrors.general ? <p className="font-body text-sm text-red-600">{profileErrors.general}</p> : null}
          <Button type="submit" disabled={updateProfile.isPending}>
            {updateProfile.isPending ? 'Saving...' : 'Save display name'}
          </Button>
        </form>
      </Card>

      <Card>
        <h2 className="font-headline text-2xl font-bold">Security</h2>
        <p className="mt-2 font-body text-slate-600 dark:text-slate-300">Change the password you use to sign in.</p>
        <form className="mt-6 space-y-4" onSubmit={handlePasswordSubmit}>
          <Input
            label="Current password"
            type="password"
            value={currentPassword}
            onChange={(event) => setCurrentPassword(event.target.value)}
            error={passwordErrors.current}
          />
          <Input
            label="New password"
            type="password"
            value={newPassword}
            onChange={(event) => setNewPassword(event.target.value)}
            error={passwordErrors.newPassword}
          />
          <Input
            label="Confirm new password"
            type="password"
            value={confirmPassword}
            onChange={(event) => setConfirmPassword(event.target.value)}
            error={passwordErrors.confirm}
          />
          {passwordErrors.general ? <p className="font-body text-sm text-red-600">{passwordErrors.general}</p> : null}
          <Button type="submit" disabled={updatePassword.isPending}>
            {updatePassword.isPending ? 'Updating...' : 'Update password'}
          </Button>
        </form>
      </Card>
    </div>
  );
}
