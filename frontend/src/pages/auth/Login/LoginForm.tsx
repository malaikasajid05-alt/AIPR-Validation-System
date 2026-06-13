import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Button } from '@/components/common/Button';
import { Input } from '@/components/common/Input';
import { getApiErrorMessage, loginRequest } from '@/services/authService';
import { useAuth } from '@/hooks/useAuth';
import { useToastStore } from '@/store/toastStore';
import { ROUTES } from '@/utils/constants';
import { validateEmail, validatePassword } from '@/utils/validators';

export function LoginForm() {
  const navigate = useNavigate();
  const { setAuth } = useAuth();
  const pushToast = useToastStore((state) => state.pushToast);
  const [form, setForm] = useState({ email: '', password: '', remember: false });
  const [showPassword, setShowPassword] = useState(false);
  const [errors, setErrors] = useState({ email: '', password: '', general: '' });
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    const emailError = validateEmail(form.email);
    const passwordError = validatePassword(form.password);
    setErrors({ email: emailError, password: passwordError, general: '' });
    if (emailError || passwordError) {
      return;
    }

    setLoading(true);
    try {
      const response = await loginRequest({ email: form.email.trim(), password: form.password });
      setAuth(response.token, { email: response.email, fullName: response.fullName });
      if (form.remember) {
        localStorage.setItem('remember-email', form.email.trim());
      } else {
        localStorage.removeItem('remember-email');
      }
      pushToast('Signed in successfully.', 'success');
      navigate(ROUTES.dashboard);
    } catch (error) {
      const message = getApiErrorMessage(error, 'Unable to sign in right now.');
      setErrors((current) => ({ ...current, general: message }));
      pushToast(message, 'error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <motion.form
      onSubmit={handleSubmit}
      className="card-surface space-y-6 p-8"
      initial={{ opacity: 0, y: 24 }}
      animate={{ opacity: 1, y: 0 }}
    >
      <div className="flex flex-col items-center text-center">
        <img src="/logo.svg" alt="AI PR Validation" className="h-14 w-14" />
        <h1 className="mt-4 font-headline text-3xl font-bold">Welcome back</h1>
        <p className="mt-2 font-body text-base text-slate-600">Sign in to your AI PR Validation account</p>
        <p className="font-body text-sm italic text-slate-500">Access your projects, reviews, and insights.</p>
      </div>

      <Input
        label="Email address"
        type="email"
        value={form.email}
        onChange={(event) => setForm((current) => ({ ...current, email: event.target.value }))}
        error={errors.email}
      />
      <Input
        label="Password"
        type={showPassword ? 'text' : 'password'}
        value={form.password}
        onChange={(event) => setForm((current) => ({ ...current, password: event.target.value }))}
        error={errors.password}
        endAdornment={
          <button type="button" className="font-headline text-xs font-semibold text-primary" onClick={() => setShowPassword((value) => !value)}>
            {showPassword ? 'Hide' : 'Show'}
          </button>
        }
      />

      <div className="flex items-center justify-between text-sm">
        <label className="flex items-center gap-2 font-body text-slate-600">
          <input
            type="checkbox"
            checked={form.remember}
            onChange={(event) => setForm((current) => ({ ...current, remember: event.target.checked }))}
          />
          Remember me
        </label>
        <Link to={ROUTES.register} className="font-headline font-semibold text-primary">
          Forgot password?
        </Link>
      </div>

      {errors.general ? <p className="rounded-lg bg-red-50 px-4 py-3 font-body text-sm text-red-700">{errors.general}</p> : null}

      <Button type="submit" className="w-full" disabled={loading}>
        {loading ? 'Signing in...' : 'Sign in to Dashboard'}
      </Button>

      <p className="text-center font-body text-sm text-slate-600">
        New to AI PR Validation?{' '}
        <Link to={ROUTES.register} className="font-headline font-semibold text-primary">
          Create an account
        </Link>
      </p>
    </motion.form>
  );
}
