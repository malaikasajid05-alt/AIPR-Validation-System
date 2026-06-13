export function Skeleton({ className }: { className: string }) {
  return <div className={`skeleton ${className}`} />;
}

export function ProjectCardSkeleton() {
  return (
    <div className="card-surface space-y-4 p-6">
      <Skeleton className="h-6 w-1/2" />
      <Skeleton className="h-4 w-full" />
      <Skeleton className="h-4 w-2/3" />
      <Skeleton className="h-10 w-32" />
    </div>
  );
}

export function DashboardStatSkeleton() {
  return (
    <div className="card-surface space-y-4 p-6">
      <Skeleton className="h-4 w-24" />
      <Skeleton className="h-10 w-20" />
      <Skeleton className="h-4 w-full" />
    </div>
  );
}
