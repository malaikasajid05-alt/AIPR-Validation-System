import { motion } from 'framer-motion';
import { Link } from 'react-router-dom';
import { Card } from '@/components/common/Card';
import type { Project } from '@/types/project.types';

export function ProjectCard({ project }: { project: Project }) {
  return (
    <motion.div whileHover={{ y: -4 }}>
      <Card className="h-full">
        <p className="font-headline text-sm font-semibold uppercase tracking-[0.16em] text-primary">Project</p>
        <h3 className="mt-3 font-headline text-2xl font-bold text-text-primary">{project.name}</h3>
        <p className="mt-3 font-body text-base text-slate-600">{project.description || 'No description provided yet.'}</p>
        <p className="mt-4 break-all font-mono text-sm text-slate-500">{project.githubRepoUrl}</p>
        <Link to={`/projects/${project.id}`} className="mt-6 inline-flex font-headline text-sm font-semibold text-primary">
          Open project
        </Link>
      </Card>
    </motion.div>
  );
}
