import { useEffect, useState } from 'react';
import { animate } from 'framer-motion';
import { Card } from '@/components/common/Card';

export function DashboardStats({
  projectCount,
  approvalRate,
}: {
  projectCount: number;
  approvalRate: number;
}) {
  const [displayProjectCount, setDisplayProjectCount] = useState(0);
  const [displayApprovalRate, setDisplayApprovalRate] = useState(0);

  useEffect(() => {
    const projectControls = animate(0, projectCount, {
      duration: 0.8,
      onUpdate: (value) => setDisplayProjectCount(Math.round(value)),
    });
    const approvalControls = animate(0, approvalRate, {
      duration: 0.8,
      onUpdate: (value) => setDisplayApprovalRate(Math.round(value)),
    });
    return () => {
      projectControls.stop();
      approvalControls.stop();
    };
  }, [approvalRate, projectCount]);

  return (
    <div className="grid gap-6 md:grid-cols-2">
      <Card>
        <p className="font-headline text-sm font-semibold uppercase tracking-[0.18em] text-primary">Projects</p>
        <p className="mt-4 font-headline text-5xl font-extrabold text-text-primary">{displayProjectCount}</p>
        <p className="mt-3 font-body text-slate-600">Repositories connected for AI validation.</p>
      </Card>
      <Card>
        <p className="font-headline text-sm font-semibold uppercase tracking-[0.18em] text-primary">Approval rate</p>
        <p className="mt-4 font-headline text-5xl font-extrabold text-text-primary">{displayApprovalRate}%</p>
        <p className="mt-3 font-body text-slate-600">Share of reviewed pull requests marked approved.</p>
      </Card>
    </div>
  );
}
