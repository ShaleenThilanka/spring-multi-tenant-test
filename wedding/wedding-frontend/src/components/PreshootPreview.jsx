import { useEffect, useMemo, useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { getPreshootGallery } from '../api/client.js';
import PhotoMasonryGrid from './PhotoMasonryGrid.jsx';

function shuffle(arr) {
  const copy = [...arr];
  for (let i = copy.length - 1; i > 0; i -= 1) {
    const j = Math.floor(Math.random() * (i + 1));
    [copy[i], copy[j]] = [copy[j], copy[i]];
  }
  return copy;
}

function MobileCarousel({ photos }) {
  const [index, setIndex] = useState(0);
  const random10 = useMemo(() => shuffle(photos).slice(0, 10), [photos]);
  const [paused, setPaused] = useState(false);

  const go = (dir) => setIndex((i) => (i + dir + random10.length) % random10.length);

  useEffect(() => {
    if (!random10.length || paused) return undefined;
    const id = setInterval(() => go(1), 3000);
    return () => clearInterval(id);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [random10.length, paused]);

  if (!random10.length) return null;

  return (
    <div
      className="relative mx-auto max-w-sm sm:hidden"
      onPointerDown={() => setPaused(true)}
      onPointerUp={() => setPaused(false)}
    >
      <div className="relative aspect-[4/5] overflow-hidden rounded-2xl border border-sage/30">
        <AnimatePresence initial={false} mode="wait">
          <motion.img
            key={random10[index].id}
            src={random10[index].filePath}
            alt=""
            drag="x"
            dragConstraints={{ left: 0, right: 0 }}
            dragElastic={0.6}
            onDragEnd={(_, info) => {
              if (info.offset.x < -60) go(1);
              else if (info.offset.x > 60) go(-1);
            }}
            initial={{ opacity: 0, x: 40 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -40 }}
            transition={{ duration: 0.35, ease: 'easeOut' }}
            className="h-full w-full cursor-grab object-cover active:cursor-grabbing"
          />
        </AnimatePresence>
      </div>
      <div className="mt-4 flex flex-wrap justify-center gap-1.5 overflow-hidden">
        {random10.map((photo, i) => (
          <button
            key={photo.id}
            onClick={() => setIndex(i)}
            aria-label={`Show photo ${i + 1}`}
            className={`h-1.5 w-1.5 rounded-full transition-colors ${i === index ? 'bg-moss' : 'bg-sage/50'}`}
          />
        ))}
      </div>
    </div>
  );
}

function DesktopCarouselGrid({ photos }) {
  return (
    <div className="hidden sm:grid sm:grid-cols-3 sm:gap-4 md:grid-cols-4">
      {photos.map((photo, i) => (
        <motion.div
          key={photo.id}
          initial={{ opacity: 0, y: 16 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true, amount: 0.3 }}
          transition={{ duration: 0.4, delay: (i % 8) * 0.05, ease: 'easeOut' }}
          className="overflow-hidden rounded-xl border border-sage/30"
        >
          <img
            src={photo.filePath}
            alt=""
            loading="lazy"
            className="aspect-square h-full w-full object-cover transition-transform duration-500 hover:scale-105"
          />
        </motion.div>
      ))}
    </div>
  );
}

export default function PreshootPreview({ layout = 'carousel' }) {
  const [photos, setPhotos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getPreshootGallery()
      .then((data) => setPhotos(Array.isArray(data) ? data : []))
      .catch(() => setPhotos([]))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return <p className="text-center font-body text-bark/60">Loading photos…</p>;
  }
  if (!photos.length) {
    return <p className="text-center font-body text-bark/60">Pre-shoot photos are on the way.</p>;
  }

  if (layout === 'grid') {
    return <PhotoMasonryGrid photos={photos} emptyLabel="Pre-shoot photos are on the way." />;
  }

  return (
    <>
      <MobileCarousel photos={photos} />
      <DesktopCarouselGrid photos={photos} />
    </>
  );
}
