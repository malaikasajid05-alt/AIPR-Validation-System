import { useMemo } from 'react';
import { DashboardStats } from './DashboardStats';
import { RecentPRReviews } from './RecentPRReviews';
import { WebhookOverviewCard } from './WebhookOverviewCard';
import { DashboardStatSkeleton } from '@/components/common/Skeleton';
import { useProjects } from '@/hooks/useProjects';
import { usePullRequests } from '@/hooks/usePullRequests';

export function DashboardPage() {
  const { data: projects = [], isLoading: projectsLoading } = useProjects();
  const { data: reviews = [] } = usePullRequests();

  const approvalRate = useMemo(() => {
    if (reviews.length === 0) {
      return 0;
    }
    const approved = reviews.filter((review) => review.status === 'APPROVED').length;
    return Math.round((approved / reviews.length) * 100);
  }, [reviews]);

  return (
    <div className="page-shell">
      <div>
        <p className="font-headline text-sm font-semibold uppercase tracking-[0.18em] text-primary">Dashboard</p>
        <h1 className="mt-2 font-headline text-4xl font-extrabold text-text-primary">Your validation command center</h1>
        <p className="mt-3 max-w-2xl font-body text-lg text-slate-600">
          Monitor project health, connect GitHub webhooks, and review AI-generated pull request feedback.
        </p>
      </div>

      {projectsLoading ? (
        <div className="grid gap-6 md:grid-cols-2">
          <DashboardStatSkeleton />
          <DashboardStatSkeleton />
        </div>
      ) : (
        <DashboardStats projectCount={projects.length} approvalRate={approvalRate} />
      )}

      <WebhookOverviewCard projectId={projects[0]?.id} />
      <RecentPRReviews />
    </div>
  );
}
