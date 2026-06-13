import { useState, type FormEvent } from 'react';
import { Button } from '@/components/common/Button';
import { Input } from '@/components/common/Input';
import { Modal } from '@/components/common/Modal';
import { useCreateSprint } from '@/hooks/useSprints';
import { useToastStore } from '@/store/toastStore';
import { getApiErrorMessage } from '@/services/authService';
import { validateRequired } from '@/utils/validators';

export function CreateSprintModal({
  open,
  projectId,
  onClose,
}: {
  open: boolean;
  projectId: number;
  onClose: () => void;
}) {
  const createSprint = useCreateSprint(projectId);
  const pushToast = useToastStore((state) => state.pushToast);
  const [form, setForm] = useState({ name: '', description: '' });
  const [errors, setErrors] = useState({ name: '', general: '' });

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    const nameError = validateRequired(form.name, 'Sprint name');
    setErrors({ name: nameError, general: '' });
    if (nameError) {
      return;
    }
    try {
      await createSprint.mutateAsync({
        name: form.name.trim(),
        description: form.description.trim(),
      });
      pushToast('Sprint created.', 'success');
      setForm({ name: '', description: '' });
      onClose();
    } catch (error) {
      const message = getApiErrorMessage(error, 'Unable to create sprint.');
      setErrors((current) => ({ ...current, general: message }));
      pushToast(message, 'error');
    }
  };

  return (
    <Modal open={open} title="Add sprint" onClose={onClose}>
      <form className="space-y-5" onSubmit={handleSubmit}>
        <Input
          label="Sprint name"
          value={form.name}
          onChange={(event) => setForm((current) => ({ ...current, name: event.target.value }))}
          error={errors.name}
        />
        <label className="block space-y-2">
          <span className="font-headline text-xs font-semibold text-slate-500">Description</span>
          <textarea
            className="input-field min-h-24"
            value={form.description}
            onChange={(event) => setForm((current) => ({ ...current, description: event.target.value }))}
          />
        </label>
        {errors.general ? <p className="font-body text-sm text-red-600">{errors.general}</p> : null}
        <Button type="submit" className="w-full" disabled={createSprint.isPending}>
          {createSprint.isPending ? 'Creating...' : 'Create sprint'}
        </Button>
      </form>
    </Modal>
  );
}
