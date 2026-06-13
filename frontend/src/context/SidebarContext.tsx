import { createContext, useContext, useMemo, useState, type ReactNode } from 'react';

interface SidebarContextValue {
  isOpen: boolean;
  openSidebar: () => void;
  closeSidebar: () => void;
  toggleSidebar: () => void;
}

const SidebarContext = createContext<SidebarContextValue | null>(null);

export function SidebarProvider({ children }: { children: ReactNode }) {
  const [isOpen, setIsOpen] = useState(false);
  const value = useMemo(
    () => ({
      isOpen,
      openSidebar: () => setIsOpen(true),
      closeSidebar: () => setIsOpen(false),
      toggleSidebar: () => setIsOpen((open) => !open),
    }),
    [isOpen],
  );

  return <SidebarContext.Provider value={value}>{children}</SidebarContext.Provider>;
}

export function useSidebar() {
  const context = useContext(SidebarContext);
  if (!context) {
    throw new Error('useSidebar must be used within SidebarProvider');
  }
  return context;
}
