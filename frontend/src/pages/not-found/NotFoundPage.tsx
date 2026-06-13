import { Link } from 'react-router-dom';
import { ROUTES } from '@/utils/constants';

export function NotFoundPage() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-white px-4 text-center">
      <p className="font-headline text-sm font-semibold uppercase tracking-[0.18em] text-primary">404</p>
      <h1 className="mt-3 font-headline text-4xl font-extrabold">Page not found</h1>
      <p className="mt-3 font-body text-slate-600">The page you requested is not available in this workspace.</p>
      <Link to={ROUTES.dashboard} className="btn-primary mt-6">
        Back to dashboard
      </Link>
    </div>
  );
}
