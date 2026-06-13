import { useProjects } from '@/hooks/useProjects';
import { WebhookOverviewCard } from '@/pages/dashboard/WebhookOverviewCard';

export function WebhooksPage() {
  const { data: projects = [] } = useProjects();

  return (
    <div className="page-shell">
      <div>
        <p className="font-headline text-sm font-semibold uppercase tracking-[0.18em] text-primary">Webhooks</p>
        <h1 className="mt-2 font-headline text-4xl font-extrabold text-text-primary">GitHub event endpoints</h1>
      </div>
      <WebhookOverviewCard projectId={projects[0]?.id} />
    </div>
  );
}
