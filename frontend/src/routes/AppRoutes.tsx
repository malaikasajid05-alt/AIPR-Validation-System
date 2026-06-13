import { Navigate, Route, Routes } from 'react-router-dom';
import { AuthLayout } from '@/layouts/AuthLayout';
import { DashboardLayout } from '@/layouts/DashboardLayout';
import { LoginPage } from '@/pages/auth/Login/LoginPage';
import { RegisterPage } from '@/pages/auth/Register/RegisterPage';
import { DashboardPage } from '@/pages/dashboard/DashboardPage';
import { ProjectsPage } from '@/pages/projects/ProjectsPage';
import { ProjectDetailsPage } from '@/pages/project-details/ProjectDetailsPage';
import { SprintDetailsPage } from '@/pages/sprints/SprintDetailsPage';
import { PRReviewsPage } from '@/pages/pr-reviews/PRReviewsPage';
import { WebhooksPage } from '@/pages/webhooks/WebhooksPage';
import { SettingsPage } from '@/pages/settings/SettingsPage';
import { NotFoundPage } from '@/pages/not-found/NotFoundPage';
import { AuthRoute } from './AuthRoute';
import { ProtectedRoute } from './ProtectedRoute';
import { ROUTES } from '@/utils/constants';

export function AppRoutes() {
  return (
    <Routes>
      <Route element={<AuthRoute />}>
        <Route element={<AuthLayout />}>
          <Route path={ROUTES.login} element={<LoginPage />} />
          <Route path={ROUTES.register} element={<RegisterPage />} />
        </Route>
      </Route>

      <Route element={<ProtectedRoute />}>
        <Route element={<DashboardLayout />}>
          <Route path={ROUTES.dashboard} element={<DashboardPage />} />
          <Route path={ROUTES.projects} element={<ProjectsPage />} />
          <Route path="/projects/:projectId" element={<ProjectDetailsPage />} />
          <Route path="/projects/:projectId/sprints/:sprintId" element={<SprintDetailsPage />} />
          <Route path={ROUTES.prReviews} element={<PRReviewsPage />} />
          <Route path={ROUTES.webhooks} element={<WebhooksPage />} />
          <Route path={ROUTES.settings} element={<SettingsPage />} />
        </Route>
      </Route>

      <Route path="/" element={<Navigate to={ROUTES.dashboard} replace />} />
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
}
