import { useCallback, useEffect, useRef, useState } from 'react';
import PhotoMasonryGrid from './PhotoMasonryGrid.jsx';

const PAGE_SIZE = 12;

export default function InfinitePhotoFeed({ fetchPage, emptyLabel }) {
  const [photos, setPhotos] = useState([]);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(true);
  const pageRef = useRef(0);
  const busyRef = useRef(false);
  const hasMoreRef = useRef(true);
  const sentinelRef = useRef(null);

  const loadMore = useCallback(async () => {
    if (busyRef.current || !hasMoreRef.current) return;
    busyRef.current = true;
    setLoading(true);
    try {
      const result = await fetchPage(pageRef.current, PAGE_SIZE);
      const incoming = Array.isArray(result.items) ? result.items : [];
      setPhotos((prev) => {
        const seen = new Set(prev.map((photo) => photo.id ?? photo.filePath));
        return [...prev, ...incoming.filter((photo) => !seen.has(photo.id ?? photo.filePath))];
      });
      const more = Boolean(result.hasMore) && incoming.length > 0;
      hasMoreRef.current = more;
      setHasMore(more);
      pageRef.current += 1;
    } catch {
      hasMoreRef.current = false;
      setHasMore(false);
    } finally {
      busyRef.current = false;
      setLoading(false);
    }
  }, [fetchPage]);

  useEffect(() => {
    pageRef.current = 0;
    hasMoreRef.current = true;
    busyRef.current = false;
    setPhotos([]);
    setHasMore(true);
    setLoading(true);
    loadMore();
  }, [loadMore]);

  useEffect(() => {
    const node = sentinelRef.current;
    if (!node) return undefined;
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0]?.isIntersecting) loadMore();
      },
      { rootMargin: '240px' }
    );
    observer.observe(node);
    return () => observer.disconnect();
  }, [loadMore, photos.length]);

  if (!loading && !photos.length) {
    return <p className="text-center font-body text-bark/60">{emptyLabel}</p>;
  }

  return (
    <div>
      <PhotoMasonryGrid photos={photos} emptyLabel={emptyLabel} />
      <div ref={sentinelRef} className="h-10 w-full" aria-hidden="true" />
      {loading && (
        <p className="mt-4 text-center font-body text-sm text-bark/50">Loading more photos…</p>
      )}
      {!hasMore && photos.length > 0 && (
        <p className="mt-4 text-center font-body text-xs uppercase tracking-[0.18em] text-bark/40">
          End of gallery
        </p>
      )}
    </div>
  );
}
