import type { ReactNode } from 'react';

interface EmptyStateProps {
  title: string;
  description: string;
  illustration?: ReactNode;
  action?: ReactNode;
}

export function EmptyState({ title, description, illustration, action }: EmptyStateProps) {
  return (
    <div className="flex flex-col items-center justify-center rounded-2xl border border-dashed border-border bg-white px-6 py-16 text-center">
      {illustration}
      <h3 className="mt-6 font-headline text-2xl font-bold text-text-primary">{title}</h3>
      <p className="mt-3 max-w-md font-body text-base text-slate-600">{description}</p>
      {action ? <div className="mt-6">{action}</div> : null}
    </div>
  );
}
