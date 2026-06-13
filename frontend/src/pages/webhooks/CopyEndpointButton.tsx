import { useState } from 'react';
import { Button } from '@/components/common/Button';
import { useToastStore } from '@/store/toastStore';

export function CopyEndpointButton({ value }: { value: string }) {
  const pushToast = useToastStore((state) => state.pushToast);
  const [copied, setCopied] = useState(false);

  const handleCopy = async () => {
    try {
      await navigator.clipboard.writeText(value);
      setCopied(true);
      pushToast('Webhook URL copied.', 'success');
      window.setTimeout(() => setCopied(false), 1800);
    } catch {
      pushToast('Unable to copy the webhook URL.', 'error');
    }
  };

  return (
    <Button type="button" variant="secondary" onClick={handleCopy}>
      {copied ? 'Copied!' : 'Copy'}
    </Button>
  );
}
