export function formatStatus(status?: string) {
  if (!status) {
    return 'Pending';
  }
  return status
    .toLowerCase()
    .split('_')
    .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
    .join(' ');
}

export function scoreColor(score: number) {
  if (score >= 80) {
    return '#16a34a';
  }
  if (score >= 50) {
    return '#f97316';
  }
  return '#dc2626';
}

export function classNames(...values: Array<string | false | null | undefined>) {
  return values.filter(Boolean).join(' ');
}
