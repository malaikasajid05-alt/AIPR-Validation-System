import { useState, type FormEvent } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button } from '@/components/common/Button';
import { Card } from '@/components/common/Card';
import { EmptyState } from '@/components/common/EmptyState';
import { Input } from '@/components/common/Input';
import { Loader } from '@/components/common/Loader';
import { useProject } from '@/hooks/useProjects';
import { useCreateRequirement, useRequirements } from '@/hooks/useRequirements';
import { useSprints } from '@/hooks/useSprints';
import { getApiErrorMessage } from '@/services/authService';
import { useToastStore } from '@/store/toastStore';
import { validateRequired } from '@/utils/validators';
import { ROUTES } from '@/utils/constants';

export function SprintDetailsPage() {
  const { projectId = '', sprintId = '' } = useParams();
  const numericProjectId = Number(projectId);
  const numericSprintId = Number(sprintId);
  const projectQuery = useProject(numericProjectId);
  const sprintsQuery = useSprints(numericProjectId);
  const requirementsQuery = useRequirements(numericSprintId);
  const createRequirement = useCreateRequirement(numericSprintId);
  const pushToast = useToastStore((state) => state.pushToast);
  const [form, setForm] = useState({ title: '', description: '' });
  const [errors, setErrors] = useState({ title: '', description: '' });

  const sprint = sprintsQuery.data?.find((item) => item.id === numericSprintId);

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    const nextErrors = {
      title: validateRequired(form.title, 'Title'),
      description: validateRequired(form.description, 'Description'),
    };
    setErrors(nextErrors);
    if (nextErrors.title || nextErrors.description) {
      return;
    }

    try {
      await createRequirement.mutateAsync({
        title: form.title.trim(),
        description: form.description.trim(),
      });
      setForm({ title: '', description: '' });
      pushToast('Requirement added.', 'success');
    } catch (error) {
      pushToast(getApiErrorMessage(error, 'Unable to add requirement.'), 'error');
    }
  };

  if (projectQuery.isLoading || sprintsQuery.isLoading) {
    return <Loader />;
  }

  return (
    <div className="page-shell">
      <nav className="font-body text-sm text-slate-600 dark:text-slate-300">
        <Link to={ROUTES.projects} className="font-semibold text-primary">
          Projects
        </Link>
        <span className="mx-2">/</span>
        <Link to={`/projects/${projectId}`} className="font-semibold text-primary">
          {projectQuery.data?.name ?? 'Project'}
        </Link>
        <span className="mx-2">/</span>
        <span>{sprint?.name ?? 'Sprint'}</span>
      </nav>

      <div className="flex flex-wrap items-center justify-between gap-4">
        <div>
          <Link to={`/projects/${projectId}`} className="font-headline text-sm font-semibold text-primary">
            Back to project
          </Link>
          <h1 className="mt-3 font-headline text-4xl font-extrabold text-text-primary">{sprint?.name || 'Sprint'}</h1>
          <p className="mt-3 max-w-2xl font-body text-lg text-slate-600">{sprint?.description}</p>
        </div>
      </div>

      <div className="grid gap-6 xl:grid-cols-[0.9fr_1.1fr]">
        <Card>
          <h2 className="font-headline text-2xl font-bold">Sprint details</h2>
          <p className="mt-4 font-body text-slate-600">Project: {projectQuery.data?.name}</p>
          <p className="mt-2 font-body text-slate-600">Use requirements below to define what AI should validate in each pull request.</p>
        </Card>

        <Card>
          <h2 className="font-headline text-2xl font-bold">Requirements</h2>
          <form className="mt-6 space-y-4" onSubmit={handleSubmit}>
            <Input
              label="Requirement title"
              value={form.title}
              onChange={(event) => setForm((current) => ({ ...current, title: event.target.value }))}
              error={errors.title}
            />
            <label className="block space-y-2">
              <span className="font-headline text-xs font-semibold text-slate-500">Description</span>
              <textarea
                className="input-field min-h-32"
                value={form.description}
                onChange={(event) => setForm((current) => ({ ...current, description: event.target.value }))}
              />
              {errors.description ? <p className="font-body text-sm text-red-600">{errors.description}</p> : null}
            </label>
            <Button type="submit" disabled={createRequirement.isPending}>
              {createRequirement.isPending ? 'Saving requirement...' : 'Add requirement'}
            </Button>
          </form>

          <div className="mt-8 space-y-4">
            {requirementsQuery.isLoading ? <Loader /> : null}
            {!requirementsQuery.isLoading && requirementsQuery.data?.length === 0 ? (
              <EmptyState
                title="No requirements added yet."
                description="Add clear, testable requirements so the backend can compare pull request changes against sprint goals."
              />
            ) : null}
            {requirementsQuery.data?.map((requirement) => (
              <article key={requirement.id} className="rounded-2xl border border-border p-4">
                <h3 className="font-headline text-xl font-semibold">{requirement.title}</h3>
                <p className="mt-2 font-body text-slate-700">{requirement.description}</p>
              </article>
            ))}
          </div>
        </Card>
      </div>
    </div>
  );
}
