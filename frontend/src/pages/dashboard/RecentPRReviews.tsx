import { motion } from 'framer-motion';
import { Link } from 'react-router-dom';
import { Card } from '@/components/common/Card';
import { Badge } from '@/components/common/Badge';
import { usePullRequests } from '@/hooks/usePullRequests';
import { formatStatus } from '@/utils/formatters';

export function RecentPRReviews() {
  const { data = [], isLoading, isError } = usePullRequests();

  if (isLoading) {
    return <Card><p className="font-body text-slate-600">Loading recent reviews...</p></Card>;
  }

  if (isError) {
    return <Card><p className="font-body text-red-700">Unable to load recent reviews right now.</p></Card>;
  }

  return (
    <Card>
      <div className="flex items-center justify-between gap-4">
        <div>
          <p className="font-headline text-sm font-semibold uppercase tracking-[0.18em] text-primary">Recent reviews</p>
          <h2 className="mt-2 font-headline text-2xl font-bold">Latest AI pull request feedback</h2>
        </div>
        <Link to="/pr-reviews" className="font-headline text-sm font-semibold text-primary">
          View all
        </Link>
      </div>

      {data.length === 0 ? (
        <p className="mt-6 font-body text-slate-600">No reviews yet. Connect your webhook to start receiving PR events.</p>
      ) : (
        <div className="mt-6 space-y-4">
          {data.slice(0, 5).map((review, index) => (
            <motion.div
              key={review.id}
              className="rounded-2xl border border-border p-4"
              initial={{ opacity: 0, y: 12 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.05 }}
            >
              <div className="flex flex-wrap items-center justify-between gap-3">
                <div>
                  <p className="font-headline text-lg font-semibold">
                    {review.projectName} #{review.prNumber}
                  </p>
                  <p className="font-body text-sm text-slate-600">{review.title}</p>
                </div>
                <Badge tone={review.status === 'APPROVED' ? 'success' : review.status === 'REJECTED' ? 'danger' : 'warning'}>
                  {formatStatus(review.status)}
                </Badge>
              </div>
              <p className="mt-3 font-body italic text-slate-700">{review.aiSummary || 'Review pending.'}</p>
            </motion.div>
          ))}
        </div>
      )}
    </Card>
  );
}
