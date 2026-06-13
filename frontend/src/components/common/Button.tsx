import type { ButtonHTMLAttributes, ReactNode } from 'react';
import { classNames } from '@/utils/formatters';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary';
  children: ReactNode;
}

export function Button({ variant = 'primary', className, children, ...props }: ButtonProps) {
  return (
    <button
      className={classNames(variant === 'primary' ? 'btn-primary' : 'btn-secondary', className)}
      {...props}
    >
      {children}
    </button>
  );
}
