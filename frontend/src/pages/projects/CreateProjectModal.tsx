import { useState, type FormEvent } from 'react';
import { Button } from '@/components/common/Button';
import { Input } from '@/components/common/Input';
import { Modal } from '@/components/common/Modal';
import { useCreateProject } from '@/hooks/useProjects';
import { useToastStore } from '@/store/toastStore';
import { getApiErrorMessage } from '@/services/authService';
import { normalizeGithubRepoUrl, validateGithubRepoUrl, validateRequired } from '@/utils/validators';

export function CreateProjectModal({
  open,
  onClose,
}: {
  open: boolean;
  onClose: () => void;
}) {
  const createProject = useCreateProject();
  const pushToast = useToastStore((state) => state.pushToast);
  const [form, setForm] = useState({ name: '', description: '', githubRepoUrl: '' });
  const [errors, setErrors] = useState({ name: '', githubRepoUrl: '', general: '' });

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    const nextErrors = {
      name: validateRequired(form.name, 'Project name'),
      githubRepoUrl: validateGithubRepoUrl(form.githubRepoUrl),
      general: '',
    };
    setErrors(nextErrors);
    if (nextErrors.name || nextErrors.githubRepoUrl) {
      return;
    }

    const normalizedRepoUrl = normalizeGithubRepoUrl(form.githubRepoUrl);

    try {
      await createProject.mutateAsync({
        name: form.name.trim(),
        description: form.description.trim(),
        githubRepoUrl: normalizedRepoUrl,
      });
      pushToast('Project created successfully.', 'success');
      setForm({ name: '', description: '', githubRepoUrl: '' });
      onClose();
    } catch (error) {
      const message = getApiErrorMessage(error, 'Unable to create the project right now.');
      setErrors((current) => ({ ...current, general: message }));
      pushToast(message, 'error');
    }
  };

  return (
    <Modal open={open} title="New Project" onClose={onClose}>
      <form className="space-y-5" onSubmit={handleSubmit}>
        <Input
          label="Project name"
          value={form.name}
          onChange={(event) => setForm((current) => ({ ...current, name: event.target.value }))}
          error={errors.name}
        />
        <label className="block space-y-2">
          <span className="font-headline text-xs font-semibold text-slate-500">Description</span>
          <textarea
            className="input-field min-h-28"
            value={form.description}
            onChange={(event) => setForm((current) => ({ ...current, description: event.target.value }))}
          />
        </label>
        <Input
          label="GitHub repository URL"
          value={form.githubRepoUrl}
          onChange={(event) => setForm((current) => ({ ...current, githubRepoUrl: event.target.value }))}
          error={errors.githubRepoUrl}
        />
        {errors.general ? <p className="font-body text-sm text-red-600">{errors.general}</p> : null}
        <Button type="submit" className="w-full" disabled={createProject.isPending}>
          {createProject.isPending ? 'Creating project...' : 'Create Project'}
        </Button>
      </form>
    </Modal>
  );
}
