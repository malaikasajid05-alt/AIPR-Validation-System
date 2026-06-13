import { useMemo, useState } from 'react';
import { Card } from '@/components/common/Card';
import { Badge } from '@/components/common/Badge';
import { CircleProgress } from '@/components/charts/CircleProgress';
import { EmptyState } from '@/components/common/EmptyState';
import { Loader } from '@/components/common/Loader';
import { usePullRequests } from '@/hooks/usePullRequests';
import { formatStatus } from '@/utils/formatters';
import type { PullRequestReview } from '@/types/pr.types';

function severityTone(severity: string) {
  if (severity === 'ERROR') {
    return 'danger' as const;
  }
  if (severity === 'WARN') {
    return 'warning' as const;
  }
  return 'info' as const;
}

function ReviewPanel({ review }: { review: PullRequestReview }) {
  return (
    <Card>
      <div className="flex flex-wrap items-start justify-between gap-6">
        <div>
          <p className="font-headline text-sm font-semibold uppercase tracking-[0.18em] text-primary">
            {review.projectName}
          </p>
          <h2 className="mt-2 font-headline text-3xl font-bold">
            PR #{review.prNumber}: {review.title}
          </h2>
          <p className="mt-2 font-body text-slate-600">Branch: {review.branch}</p>
        </div>
        <div className="flex items-center gap-4">
          <CircleProgress score={review.score ?? 0} />
          <Badge
            tone={
              review.status === 'APPROVED'
                ? 'success'
                : review.status === 'REJECTED'
                  ? 'danger'
                  : 'warning'
            }
          >
            {formatStatus(review.status)}
          </Badge>
        </div>
      </div>

      <p className="mt-8 font-body text-xl italic leading-relaxed text-slate-700">
        {review.aiSummary || 'No AI summary available yet.'}
      </p>

      {review.missingRequirements?.length ? (
        <div className="mt-8">
          <h3 className="font-headline text-xl font-bold">Missing requirements</h3>
          <ul className="mt-4 list-disc space-y-2 pl-5 font-body text-slate-700">
            {review.missingRequirements.map((item) => (
              <li key={item}>{item}</li>
            ))}
          </ul>
        </div>
      ) : null}

      {review.inlineComments?.length ? (
        <div className="mt-8">
          <h3 className="font-headline text-xl font-bold">Inline comments</h3>
          <div className="mt-4 space-y-4">
            {review.inlineComments.map((comment, index) => (
              <article key={`${comment.file}-${comment.line}-${index}`} className="rounded-2xl border border-border p-4">
                <p className="font-mono text-sm text-slate-600">
                  {comment.file}:{comment.line}
                </p>
                <p className="mt-3 font-body text-slate-700">{comment.comment}</p>
                <div className="mt-3">
                  <Badge tone={severityTone(comment.severity)}>{comment.severity}</Badge>
                </div>
              </article>
            ))}
          </div>
        </div>
      ) : null}

      {review.githubPrUrl ? (
        <a
          href={review.githubPrUrl}
          target="_blank"
          rel="noreferrer"
          className="mt-8 inline-flex rounded-lg bg-primary px-4 py-3 font-headline text-sm font-semibold text-white"
        >
          Open GitHub pull request
        </a>
      ) : null}
    </Card>
  );
}

export function PRReviewsPage() {
  const { data = [], isLoading, isError, refetch } = usePullRequests();
  const [selectedId, setSelectedId] = useState<number | null>(null);

  const selectedReview = useMemo(
    () => data.find((review) => review.id === selectedId) ?? data[0],
    [data, selectedId],
  );

  if (isLoading) {
    return <Loader />;
  }

  if (isError) {
    return (
      <EmptyState
        title="Unable to load PR reviews"
        description="We could not fetch stored AI review results. Try again in a moment."
        action={
          <button type="button" className="btn-primary" onClick={() => refetch()}>
            Retry
          </button>
        }
      />
    );
  }

  return (
    <div className="page-shell">
      <div>
        <p className="font-headline text-sm font-semibold uppercase tracking-[0.18em] text-primary">PR Reviews</p>
        <h1 className="mt-2 font-headline text-4xl font-extrabold text-text-primary">AI validation intelligence</h1>
      </div>

      {data.length === 0 ? (
        <EmptyState
          title="No pull request reviews yet"
          description="Once GitHub sends webhook events, the backend stores AI scores, summaries, and inline comments here."
          illustration={<img src="/illustrations/empty-prs.svg" alt="" className="h-28 w-28" />}
        />
      ) : (
        <div className="grid gap-6 xl:grid-cols-[0.8fr_1.2fr]">
          <Card>
            <h2 className="font-headline text-2xl font-bold">Stored reviews</h2>
            <div className="mt-6 space-y-3">
              {data.map((review) => (
                <button
                  key={review.id}
                  type="button"
                  onClick={() => setSelectedId(review.id)}
                  className="w-full rounded-2xl border border-border px-4 py-4 text-left transition hover:border-primary"
                >
                  <p className="font-headline font-semibold">
                    {review.projectName} #{review.prNumber}
                  </p>
                  <p className="mt-1 font-body text-sm text-slate-600">{review.title}</p>
                </button>
              ))}
            </div>
          </Card>
          {selectedReview ? <ReviewPanel review={selectedReview} /> : null}
        </div>
      )}
    </div>
  );
}
