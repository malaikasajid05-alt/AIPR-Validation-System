import { useMemo, useState } from 'react';
import { Card } from '@/components/common/Card';
import { Button } from '@/components/common/Button';
import { CopyEndpointButton } from '@/pages/webhooks/CopyEndpointButton';
import { buildBackendWebhookUrl, buildWebhookUrl, regenerateProjectWebhook } from '@/services/webhookService';
import { useToastStore } from '@/store/toastStore';

export function WebhookOverviewCard({ projectId }: { projectId?: number }) {
  const pushToast = useToastStore((state) => state.pushToast);
  const [displayUrl, setDisplayUrl] = useState(() =>
    projectId ? buildWebhookUrl(projectId) : buildBackendWebhookUrl(),
  );

  const backendUrl = useMemo(() => buildBackendWebhookUrl(), []);

  const handleRegenerate = () => {
    if (!projectId) {
      pushToast('Create a project to generate a project-specific webhook URL.', 'info');
      return;
    }
    const nextUrl = regenerateProjectWebhook(projectId);
    setDisplayUrl(nextUrl);
    pushToast('Webhook endpoint regenerated.', 'success');
  };

  return (
    <Card>
      <p className="font-headline text-sm font-semibold uppercase tracking-[0.18em] text-primary">Webhook Endpoint</p>
      <h2 className="mt-2 font-headline text-2xl font-bold text-text-primary">Connect GitHub PR events</h2>
      <p className="mt-3 font-body text-base text-slate-600">
        Use the following endpoint to send PR events to our system.
      </p>

      <div className="mt-6 rounded-2xl border border-border bg-slate-50 p-4">
        <p className="font-headline text-xs font-semibold uppercase tracking-[0.16em] text-slate-500">Display URL</p>
        <p className="mt-2 break-all font-mono text-sm text-text-primary">{displayUrl}</p>
        <div className="mt-4 flex flex-wrap gap-3">
          <CopyEndpointButton value={displayUrl} />
          <Button type="button" variant="secondary" onClick={handleRegenerate}>
            Regenerate
          </Button>
        </div>
      </div>

      <div className="mt-4 rounded-2xl border border-border bg-white p-4">
        <p className="font-headline text-xs font-semibold uppercase tracking-[0.16em] text-slate-500">Backend ingest URL</p>
        <p className="mt-2 break-all font-mono text-sm text-text-primary">{backendUrl}</p>
        <div className="mt-4">
          <CopyEndpointButton value={backendUrl} />
        </div>
      </div>
    </Card>
  );
}
