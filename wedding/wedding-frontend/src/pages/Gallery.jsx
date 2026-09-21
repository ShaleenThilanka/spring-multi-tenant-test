import { useCallback, useState } from 'react';
import { motion } from 'framer-motion';
import PageShell from '../components/PageShell.jsx';
import GuestNav from '../components/GuestNav.jsx';
import VineDivider from '../components/VineDivider.jsx';
import MemoryUploadForm from '../components/MemoryUploadForm.jsx';
import NameFlourish from '../components/NameFlourish.jsx';
import InfinitePhotoFeed from '../components/InfinitePhotoFeed.jsx';
import { getGuestUploadsGalleryPage, getPreshootGalleryPage } from '../api/client.js';
import { fadeUp, stagger } from '../lib/motion.js';

const TABS = [
  { key: 'preshoot', label: 'Pre-Shoot' },
  { key: 'moments', label: 'Our Moments' },
];

export default function Gallery() {
  const [tab, setTab] = useState('preshoot');

  const fetchPreshoot = useCallback((page, size) => getPreshootGalleryPage(page, size), []);
  const fetchMoments = useCallback((page, size) => getGuestUploadsGalleryPage(page, size), []);

  return (
    <PageShell className="bg-ivory">
      <div className="relative bg-ivory pb-8">
        <GuestNav />
        <motion.div
          className="px-5 text-center sm:px-10"
          variants={stagger}
          initial="hidden"
          animate="show"
        >
          <motion.p variants={fadeUp} className="font-body text-[0.65rem] uppercase tracking-[0.3em] text-moss">
            Memories
          </motion.p>
          <motion.h1 variants={fadeUp} className="mt-2 font-display text-4xl italic text-canopy">
            Gallery
          </motion.h1>
          <NameFlourish className="mx-auto mt-2 h-8 w-48" />
        </motion.div>
      </div>

      <section className="relative mx-auto max-w-5xl px-5 py-12 sm:px-10 sm:py-16">
        <div className="flex justify-center gap-3">
          {TABS.map((item) => (
            <button
              key={item.key}
              onClick={() => setTab(item.key)}
              className={`rounded-full px-5 py-2 font-body text-sm uppercase tracking-[0.15em] transition-colors ${
                tab === item.key ? 'bg-moss text-ivory' : 'bg-sage/20 text-bark/70 hover:bg-sage/30'
              }`}
              aria-pressed={tab === item.key}
            >
              {item.label}
            </button>
          ))}
        </div>

        <div className="mt-10">
          {tab === 'preshoot' ? (
            <InfinitePhotoFeed
              fetchPage={fetchPreshoot}
              emptyLabel="Pre-shoot photos are on the way."
            />
          ) : (
            <InfinitePhotoFeed
              fetchPage={fetchMoments}
              emptyLabel="No moments shared yet — be the first!"
            />
          )}
        </div>

        <VineDivider className="mt-16 bg-ivory" />

        <MemoryUploadForm
          title="Upload your best memory"
          subtitle="Took a great shot on the wedding day? Share it and we'll add it to Our Moments."
        />
      </section>
    </PageShell>
  );
}
