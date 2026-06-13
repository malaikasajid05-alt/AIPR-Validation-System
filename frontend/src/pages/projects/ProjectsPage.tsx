import { useState } from 'react';
import { Button } from '@/components/common/Button';
import { EmptyState } from '@/components/common/EmptyState';
import { ProjectCardSkeleton } from '@/components/common/Skeleton';
import { useProjects } from '@/hooks/useProjects';
import { CreateProjectModal } from './CreateProjectModal';
import { ProjectCard } from './ProjectCard';

export function ProjectsPage() {
  const { data = [], isLoading, isError, refetch } = useProjects();
  const [open, setOpen] = useState(false);

  return (
    <div className="page-shell">
      <div className="flex flex-wrap items-center justify-between gap-4">
        <div>
          <p className="font-headline text-sm font-semibold uppercase tracking-[0.18em] text-primary">Projects</p>
          <h1 className="mt-2 font-headline text-4xl font-extrabold text-text-primary">Manage connected repositories</h1>
        </div>
        <Button type="button" onClick={() => setOpen(true)}>
          + New Project
        </Button>
      </div>

      {isLoading ? (
        <div className="grid gap-6 md:grid-cols-2">
          <ProjectCardSkeleton />
          <ProjectCardSkeleton />
        </div>
      ) : null}

      {isError ? (
        <EmptyState
          title="Unable to load projects"
          description="We could not fetch your projects. Check your connection and try again."
          action={
            <Button type="button" onClick={() => refetch()}>
              Retry
            </Button>
          }
        />
      ) : null}

      {!isLoading && !isError && data.length === 0 ? (
        <EmptyState
          title="No projects yet. Create your first project."
          description="Connect a GitHub repository, define sprint requirements, and let the backend validate incoming pull requests."
          illustration={<img src="/illustrations/empty-projects.svg" alt="" className="h-28 w-28" />}
          action={
            <Button type="button" onClick={() => setOpen(true)}>
              + New Project
            </Button>
          }
        />
      ) : null}

      {!isLoading && !isError && data.length > 0 ? (
        <div className="grid gap-6 md:grid-cols-2">
          {data.map((project) => (
            <ProjectCard key={project.id} project={project} />
          ))}
        </div>
      ) : null}

      <CreateProjectModal open={open} onClose={() => setOpen(false)} />
    </div>
  );
}
