import type { ReactNode } from 'react';
import { classNames } from '@/utils/formatters';

export function Badge({
  children,
  tone = 'neutral',
}: {
  children: ReactNode;
  tone?: 'neutral' | 'success' | 'warning' | 'danger' | 'info';
}) {
  const styles = {
    neutral: 'bg-slate-100 text-slate-700',
    success: 'bg-emerald-100 text-emerald-700',
    warning: 'bg-orange-100 text-orange-700',
    danger: 'bg-red-100 text-red-700',
    info: 'bg-sky-100 text-sky-700',
  }[tone];

  return (
    <span className={classNames('inline-flex rounded-full px-3 py-1 font-headline text-xs font-semibold uppercase', styles)}>
      {children}
    </span>
  );
}
