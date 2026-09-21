import { AnimatePresence, motion } from 'framer-motion';
import { useEffect, useState } from 'react';
import { getAgenda } from '../api/client.js';
import { siteConfig } from '../config/siteConfig';

export default function AgendaModal({ open, onClose }) {
  const [items, setItems] = useState(siteConfig.agenda);

  useEffect(() => {
    if (!open) return;
    const onKey = (e) => e.key === 'Escape' && onClose();
    window.addEventListener('keydown', onKey);
    return () => window.removeEventListener('keydown', onKey);
  }, [open, onClose]);

  useEffect(() => {
    if (!open) return undefined;
    let cancelled = false;
    getAgenda()
      .then((data) => {
        if (!cancelled && Array.isArray(data) && data.length) {
          setItems(data);
        }
      })
      .catch(() => {
        if (!cancelled) setItems(siteConfig.agenda);
      });
    return () => {
      cancelled = true;
    };
  }, [open]);

  return (
    <AnimatePresence>
      {open && (
        <motion.div
          className="fixed inset-0 z-50 flex items-center justify-center bg-ivory/80 px-4 backdrop-blur-sm"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          onClick={onClose}
          role="dialog"
          aria-modal="true"
          aria-label="Wedding day agenda"
        >
          <motion.div
            className="max-h-[85vh] w-full max-w-lg overflow-y-auto rounded-2xl border border-moss/20 bg-ivory p-8 text-bark shadow-2xl"
            initial={{ opacity: 0, y: 24, scale: 0.97 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: 12, scale: 0.98 }}
            transition={{ duration: 0.35, ease: 'easeOut' }}
            onClick={(e) => e.stopPropagation()}
          >
            <div className="mb-6 flex items-start justify-between gap-4">
              <h3 className="font-display text-3xl italic text-canopy">The Day's Agenda</h3>
              <button
                onClick={onClose}
                aria-label="Close agenda"
                className="rounded-full p-1 text-bark/50 hover:text-moss"
              >
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
                  <path d="M4 4 L16 16 M16 4 L4 16" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
                </svg>
              </button>
            </div>
            <ol className="space-y-5">
              {items.map((item) => (
                <li key={item.id ?? item.title} className="border-l-2 border-moss/40 pl-4">
                  <p className="font-body text-xs uppercase tracking-[0.2em] text-moss">{item.time}</p>
                  <p className="font-display text-xl text-canopy">{item.title}</p>
                  <p className="font-body text-sm text-bark/70">{item.description}</p>
                </li>
              ))}
            </ol>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
}
