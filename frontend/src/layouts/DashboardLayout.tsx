import { Outlet } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Sidebar } from '@/components/sidebar/Sidebar';
import { ToastViewport } from '@/components/common/ToastViewport';
import { SidebarProvider, useSidebar } from '@/context/SidebarContext';

function DashboardShell() {
  const { toggleSidebar } = useSidebar();

  return (
    <div className="min-h-screen bg-white lg:flex">
      <Sidebar />
      <div className="flex min-h-screen flex-1 flex-col">
        <header className="sticky top-0 z-30 flex items-center justify-between border-b border-border bg-white px-4 py-4 lg:px-8">
          <button
            type="button"
            className="rounded-lg border border-border px-3 py-2 font-headline text-sm font-semibold lg:hidden"
            onClick={toggleSidebar}
          >
            Menu
          </button>
          <div className="hidden lg:block" />
          <p className="font-body text-sm text-slate-600">AI-powered pull request validation</p>
        </header>
        <main className="flex-1 px-4 py-8 lg:px-8">
          <motion.div initial={{ opacity: 0, y: 16 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.3 }}>
            <Outlet />
          </motion.div>
        </main>
      </div>
      <ToastViewport />
    </div>
  );
}

export function DashboardLayout() {
  return (
    <SidebarProvider>
      <DashboardShell />
    </SidebarProvider>
  );
}
