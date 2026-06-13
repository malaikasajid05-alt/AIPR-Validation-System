import { useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Card } from '@/components/common/Card';
import { Button } from '@/components/common/Button';
import { EmptyState } from '@/components/common/EmptyState';
import { Loader } from '@/components/common/Loader';
import { useProject } from '@/hooks/useProjects';
import { useSprints } from '@/hooks/useSprints';
import { useProjectPullRequests } from '@/hooks/usePullRequests';
import { CreateSprintModal } from '@/pages/sprints/CreateSprintModal';
import { ROUTES } from '@/utils/constants';
import { formatStatus } from '@/utils/formatters';

export function ProjectDetailsPage() {
  const { projectId = '' } = useParams();
  const numericProjectId = Number(projectId);
  const projectQuery = useProject(numericProjectId);
  const sprintsQuery = useSprints(numericProjectId);
  const pullRequestsQuery = useProjectPullRequests(numericProjectId);
  const [sprintModalOpen, setSprintModalOpen] = useState(false);

  if (projectQuery.isLoading) {
    return <Loader />;
  }

  if (projectQuery.isError || !projectQuery.data) {
    return (
      <EmptyState
        title="Project unavailable"
        description="We could not load this project. It may have been removed or you may not have access."
      />
    );
  }

  const project = projectQuery.data;

  return (
    <div className="page-shell">
      <nav className="font-body text-sm text-slate-600 dark:text-slate-300">
        <Link to={ROUTES.projects} className="font-semibold text-primary">
          Projects
        </Link>
        <span className="mx-2">/</span>
        <span>{project.name}</span>
      </nav>

      <div className="flex flex-wrap items-start justify-between gap-4">
        <div>
          <p className="font-headline text-sm font-semibold uppercase tracking-[0.18em] text-primary">Project</p>
          <h1 className="mt-2 font-headline text-4xl font-extrabold text-text-primary">{project.name}</h1>
          <p className="mt-3 max-w-3xl font-body text-lg text-slate-600 dark:text-slate-300">{project.description}</p>
          <p className="mt-2 break-all font-mono text-sm text-slate-500 dark:text-slate-400">{project.githubRepoUrl}</p>
        </div>
        <div className="flex flex-wrap gap-3">
          <Button type="button" onClick={() => setSprintModalOpen(true)}>
            + Add sprint
          </Button>
          <Link to={ROUTES.prReviews} className="btn-secondary">
            PR reviews
          </Link>
        </div>
      </div>

      <div className="grid gap-6 xl:grid-cols-[1.2fr_0.8fr]">
        <Card>
          <div className="flex items-center justify-between gap-4">
            <h2 className="font-headline text-2xl font-bold">Sprints</h2>
            <Button type="button" variant="secondary" onClick={() => setSprintModalOpen(true)}>
              New sprint
            </Button>
          </div>
          <p className="mt-2 font-body text-sm text-slate-600 dark:text-slate-300">
            Open a sprint to add requirements. Flow: Project → Sprints → Requirements.
          </p>
          {sprintsQuery.isLoading ? <Loader /> : null}
          {sprintsQuery.data?.length === 0 ? (
            <p className="mt-4 font-body text-slate-600 dark:text-slate-300">No sprints yet. Create one to capture requirements.</p>
          ) : (
            <div className="mt-6 space-y-4">
              {sprintsQuery.data?.map((sprint) => (
                <Link
                  key={sprint.id}
                  to={`/projects/${project.id}/sprints/${sprint.id}`}
                  className="block rounded-2xl border border-border p-4 transition hover:border-primary dark:border-slate-600"
                >
                  <p className="font-headline text-xl font-semibold">{sprint.name}</p>
                  <p className="mt-2 font-body text-slate-600 dark:text-slate-300">
                    {sprint.description || 'No sprint description yet.'}
                  </p>
                  <p className="mt-3 font-headline text-sm font-semibold text-primary">Manage requirements →</p>
                </Link>
              ))}
            </div>
          )}
        </Card>

        <Card>
          <h2 className="font-headline text-2xl font-bold">Recent reviews</h2>
          {pullRequestsQuery.isLoading ? <Loader /> : null}
          {pullRequestsQuery.data?.length === 0 ? (
            <p className="mt-4 font-body text-slate-600 dark:text-slate-300">No pull request reviews stored for this project yet.</p>
          ) : (
            <div className="mt-6 space-y-4">
              {pullRequestsQuery.data?.map((review) => (
                <div key={review.id} className="rounded-2xl border border-border p-4 dark:border-slate-600">
                  <p className="font-headline font-semibold">
                    #{review.prNumber} {review.title}
                  </p>
                  <p className="mt-2 font-body italic text-slate-700 dark:text-slate-200">{review.aiSummary}</p>
                  <p className="mt-2 font-headline text-sm text-slate-500">{formatStatus(review.status)}</p>
                </div>
              ))}
            </div>
          )}
        </Card>
      </div>

      <CreateSprintModal open={sprintModalOpen} projectId={numericProjectId} onClose={() => setSprintModalOpen(false)} />
    </div>
  );
}
