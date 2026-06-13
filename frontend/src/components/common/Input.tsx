import type { InputHTMLAttributes, ReactNode } from 'react';
import { classNames } from '@/utils/formatters';

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string;
  error?: string;
  endAdornment?: ReactNode;
}

export function Input({ label, error, endAdornment, className, id, ...props }: InputProps) {
  const inputId = id || label.toLowerCase().replace(/\s+/g, '-');
  return (
    <label className="block space-y-2" htmlFor={inputId}>
      <div className="relative">
        <span className="floating-label">{label}</span>
        <input id={inputId} className={classNames('input-field', endAdornment ? 'pr-12' : '', className)} {...props} />
        {endAdornment ? <div className="absolute right-3 top-1/2 -translate-y-1/2">{endAdornment}</div> : null}
      </div>
      {error ? <p className="font-body text-sm text-red-600">{error}</p> : null}
    </label>
  );
}
