import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Button } from '@/components/common/Button';
import { Input } from '@/components/common/Input';
import { getApiErrorMessage, registerRequest } from '@/services/authService';
import { useToastStore } from '@/store/toastStore';
import { ROUTES } from '@/utils/constants';
import {
  validateConfirmPassword,
  validateEmail,
  validatePassword,
  validateRequired,
} from '@/utils/validators';

export function RegisterForm() {
  const navigate = useNavigate();
  const pushToast = useToastStore((state) => state.pushToast);
  const [form, setForm] = useState({
    fullName: '',
    email: '',
    password: '',
    confirmPassword: '',
  });
  const [errors, setErrors] = useState({
    fullName: '',
    email: '',
    password: '',
    confirmPassword: '',
    general: '',
  });
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    const nextErrors = {
      fullName: validateRequired(form.fullName, 'Full name'),
      email: validateEmail(form.email),
      password: validatePassword(form.password),
      confirmPassword: validateConfirmPassword(form.password, form.confirmPassword),
      general: '',
    };
    setErrors(nextErrors);
    if (Object.values(nextErrors).some(Boolean)) {
      return;
    }

    setLoading(true);
    try {
      await registerRequest({
        fullName: form.fullName.trim(),
        email: form.email.trim(),
        password: form.password,
      });
      pushToast('Account created. Sign in to continue.', 'success');
      navigate(ROUTES.login);
    } catch (error) {
      const message = getApiErrorMessage(error, 'Unable to create your account right now.');
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
        <h1 className="mt-4 font-headline text-3xl font-bold">Create account</h1>
        <p className="mt-2 font-body text-base text-slate-600">Start validating pull requests with AI</p>
      </div>

      <Input
        label="Full name"
        value={form.fullName}
        onChange={(event) => setForm((current) => ({ ...current, fullName: event.target.value }))}
        error={errors.fullName}
      />
      <Input
        label="Email address"
        type="email"
        value={form.email}
        onChange={(event) => setForm((current) => ({ ...current, email: event.target.value }))}
        error={errors.email}
      />
      <Input
        label="Password"
        type="password"
        value={form.password}
        onChange={(event) => setForm((current) => ({ ...current, password: event.target.value }))}
        error={errors.password}
      />
      <Input
        label="Confirm password"
        type="password"
        value={form.confirmPassword}
        onChange={(event) => setForm((current) => ({ ...current, confirmPassword: event.target.value }))}
        error={errors.confirmPassword}
      />

      {errors.general ? <p className="rounded-lg bg-red-50 px-4 py-3 font-body text-sm text-red-700">{errors.general}</p> : null}

      <Button type="submit" className="w-full" disabled={loading}>
        {loading ? 'Creating account...' : 'Create Account'}
      </Button>

      <p className="text-center font-body text-sm text-slate-600">
        Already have an account?{' '}
        <Link to={ROUTES.login} className="font-headline font-semibold text-primary">
          Sign in
        </Link>
      </p>
    </motion.form>
  );
}
