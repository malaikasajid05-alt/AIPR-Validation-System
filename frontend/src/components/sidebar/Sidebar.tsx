import { NavLink } from 'react-router-dom';
import { useAuth } from '@/hooks/useAuth';
import { useSidebar } from '@/context/SidebarContext';
import { ROUTES } from '@/utils/constants';
import { classNames } from '@/utils/formatters';

const mainItems = [
  { label: 'Dashboard', to: ROUTES.dashboard },
  { label: 'Projects', to: ROUTES.projects },
];

const manageItems = [
  { label: 'PR Reviews', to: ROUTES.prReviews },
  { label: 'Webhooks', to: ROUTES.webhooks },
  { label: 'Settings', to: ROUTES.settings },
];

function SidebarLink({ to, label, onNavigate }: { to: string; label: string; onNavigate?: () => void }) {
  return (
    <NavLink
      to={to}
      onClick={onNavigate}
      className={({ isActive }) =>
        classNames(
          'block rounded-lg px-4 py-3 font-headline text-sm font-semibold transition',
          isActive ? 'bg-primary text-white' : 'text-white/85 hover:bg-white/10',
        )
      }
    >
      {label}
    </NavLink>
  );
}

export function Sidebar() {
  const { user, clearAuth } = useAuth();
  const { isOpen, closeSidebar } = useSidebar();

  return (
    <>
      <div
        className={classNames(
          'fixed inset-0 z-40 bg-black/40 lg:hidden',
          isOpen ? 'block' : 'hidden',
        )}
        onClick={closeSidebar}
      />
      <aside
        className={classNames(
          'fixed inset-y-0 left-0 z-50 flex w-72 flex-col bg-accent px-5 py-6 text-white transition-transform lg:sticky lg:translate-x-0',
          isOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0',
        )}
      >
        <div className="mb-10 flex items-center gap-3">
          <img src="/logo.svg" alt="AI PR Validation" className="h-10 w-10" />
          <div>
            <p className="font-headline text-lg font-bold">AI PR Validation</p>
            <p className="font-body text-sm text-white/70">Review intelligence</p>
          </div>
        </div>

        <nav className="space-y-2">
          {mainItems.map((item) => (
            <SidebarLink key={item.to} to={item.to} label={item.label} onNavigate={closeSidebar} />
          ))}
        </nav>

        <p className="mb-2 mt-8 font-headline text-xs font-semibold uppercase tracking-[0.2em] text-white/60">
          Manage
        </p>
        <nav className="space-y-2">
          {manageItems.map((item) => (
            <SidebarLink key={item.to} to={item.to} label={item.label} onNavigate={closeSidebar} />
          ))}
        </nav>

        <div className="mt-auto rounded-2xl bg-white/10 p-4">
          <p className="font-headline text-sm font-semibold">{user?.fullName}</p>
          <p className="font-body text-sm text-white/70">{user?.email}</p>
          <button
            type="button"
            className="mt-4 w-full rounded-lg bg-white/15 px-4 py-2 font-headline text-sm font-semibold"
            onClick={clearAuth}
          >
            Sign out
          </button>
        </div>
      </aside>
    </>
  );
}
