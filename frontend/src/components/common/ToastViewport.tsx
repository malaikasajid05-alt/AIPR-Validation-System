import { useToastStore } from '@/store/toastStore';
import { classNames } from '@/utils/formatters';

export function ToastViewport() {
  const toasts = useToastStore((state) => state.toasts);
  const dismissToast = useToastStore((state) => state.dismissToast);

  return (
    <div className="pointer-events-none fixed right-4 top-4 z-[100] flex w-full max-w-sm flex-col gap-3">
      {toasts.map((toast) => (
        <button
          key={toast.id}
          type="button"
          onClick={() => dismissToast(toast.id)}
          className={classNames(
            'pointer-events-auto rounded-xl border px-4 py-3 text-left font-body text-sm shadow-card',
            toast.tone === 'success' && 'border-emerald-200 bg-emerald-50 text-emerald-900',
            toast.tone === 'error' && 'border-red-200 bg-red-50 text-red-900',
            toast.tone === 'info' && 'border-border bg-white text-text-primary',
          )}
        >
          {toast.message}
        </button>
      ))}
    </div>
  );
}
