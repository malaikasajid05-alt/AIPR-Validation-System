import type { ReactNode } from 'react';
import { classNames } from '@/utils/formatters';

export function Card({ children, className }: { children: ReactNode; className?: string }) {
  return <section className={classNames('card-surface p-6', className)}>{children}</section>;
}
