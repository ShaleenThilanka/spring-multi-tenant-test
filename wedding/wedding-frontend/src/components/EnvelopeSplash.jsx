import { useEffect, useState } from 'react';
import { AnimatePresence, motion } from 'framer-motion';
import { useLocation } from 'react-router-dom';
import { siteConfig } from '../config/siteConfig';

function WaxSeal({ initials }) {
  return (
    <svg viewBox="0 0 96 96" className="h-[4.5rem] w-[4.5rem] drop-shadow-[0_8px_16px_rgba(74,18,32,0.35)] sm:h-20 sm:w-20" aria-hidden="true">
      <circle cx="48" cy="48" r="46" fill="#6B1C2C" />
      <circle cx="48" cy="48" r="40" fill="#8B2E3E" />
      <circle cx="48" cy="48" r="34" fill="none" stroke="#F7EFE6" strokeWidth="1.4" opacity="0.7" />
      <text
        x="48"
        y="55"
        textAnchor="middle"
        fill="#F6F1E4"
        fontFamily="Cormorant Garamond, serif"
        fontSize="20"
        fontStyle="italic"
      >
        {initials}
      </text>
    </svg>
  );
}

export default function EnvelopeSplash() {
  const { pathname } = useLocation();
  const isAdmin = pathname.startsWith('/admin');
  const [phase, setPhase] = useState('sealed');

  const { couple, wedding, venue, splash } = siteConfig;
  const sealedMs = splash.sealedMs ?? 3000;
  const isOpen = phase !== 'sealed';
  const showCard = phase === 'sliding' || phase === 'revealed';
  const initials = `${couple.partnerOne[0]}&${couple.partnerTwo[0]}`;

  useEffect(() => {
    if (isAdmin) return undefined;
    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
      setPhase('gone');
    }
    return undefined;
  }, [isAdmin]);

  useEffect(() => {
    if (isAdmin || phase !== 'sealed') return undefined;
    const t = setTimeout(() => setPhase('opening'), sealedMs);
    return () => clearTimeout(t);
  }, [isAdmin, phase, sealedMs]);

  useEffect(() => {
    if (phase !== 'opening') return undefined;
    const t = setTimeout(() => setPhase('sliding'), 650);
    return () => clearTimeout(t);
  }, [phase]);

  useEffect(() => {
    if (phase !== 'sliding') return undefined;
    const t = setTimeout(() => setPhase('revealed'), 1400);
    return () => clearTimeout(t);
  }, [phase]);

  useEffect(() => {
    if (phase !== 'revealed') return undefined;
    const t = setTimeout(() => setPhase('gone'), 2800);
    return () => clearTimeout(t);
  }, [phase]);

  useEffect(() => {
    if (isAdmin || phase === 'gone') {
      document.body.style.overflow = '';
      return undefined;
    }
    const prev = document.body.style.overflow;
    document.body.style.overflow = 'hidden';
    return () => {
      document.body.style.overflow = prev;
    };
  }, [isAdmin, phase]);

  const openNow = () => {
    if (phase === 'sealed') setPhase('opening');
  };

  if (isAdmin) return null;

  return (
    <AnimatePresence>
      {phase !== 'gone' && (
        <motion.div
          key="envelope-splash"
          className="fixed inset-0 z-[100] cursor-pointer overflow-hidden bg-[#f7efe6]"
          initial={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 1 }}
          onClick={openNow}
          onKeyDown={(event) => {
            if (event.key === 'Enter' || event.key === ' ') {
              event.preventDefault();
              openNow();
            }
          }}
          role="dialog"
          aria-modal="true"
          aria-label="Wedding invitation envelope"
          tabIndex={0}
        >
          <div
            className="pointer-events-none absolute inset-0 opacity-40"
            style={{
              backgroundImage:
                'radial-gradient(rgba(139,46,62,0.08) 0.8px, transparent 0.8px)',
              backgroundSize: '4px 4px',
            }}
          />

          {/* Invitation photo — revealed as the envelope opens */}
          <div className="absolute inset-0 z-[1] flex items-center justify-center p-5 sm:p-8">
            <motion.article
              className="relative h-full w-full max-w-sm overflow-hidden rounded-sm border border-gold/30 shadow-2xl"
              initial={false}
              animate={{ opacity: showCard ? 1 : 0, y: showCard ? 0 : 28 }}
              transition={{ duration: 0.9, ease: [0.22, 1, 0.36, 1] }}
            >
              <img
                src={splash.image}
                alt={`${couple.partnerOne} and ${couple.partnerTwo}`}
                className="h-full w-full object-cover"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-canopy/85 via-canopy/15 to-transparent" />
              <div className="absolute inset-x-0 bottom-0 px-5 pb-8 text-center text-ivory">
                <p className="font-body text-[0.65rem] uppercase tracking-[0.32em] text-sage">Together</p>
                <h1 className="mt-2 font-display text-3xl italic leading-tight">
                  {couple.partnerOne} <span className="not-italic text-gold">&amp;</span> {couple.partnerTwo}
                </h1>
                <p className="mt-2 font-display text-xl text-ivory/95">{wedding.displayDate}</p>
                <p className="mt-1 font-body text-xs text-ivory/70">{venue.name}</p>
              </div>
            </motion.article>
          </div>

          {/* Bottom of the envelope */}
          <motion.div
            className="absolute inset-x-0 bottom-0 z-[2] h-[58%]"
            style={{
              clipPath: 'polygon(0 22%, 50% 0, 100% 22%, 100% 100%, 0 100%)',
              backgroundColor: '#f7efe6',
              boxShadow: '0 -12px 40px rgba(59,46,36,0.08)',
            }}
            animate={{ y: isOpen ? '110%' : '0%' }}
            transition={{ duration: 0.85, ease: [0.22, 1, 0.36, 1] }}
          >
            <div className="flex h-full flex-col items-center justify-end px-6 pb-[18%]">
              <p className="font-display text-2xl italic text-moss">
                {couple.partnerOne} &amp; {couple.partnerTwo}
              </p>
              <p className="mt-2 text-center font-display text-base italic text-moss/80">
                Request the pleasure of your company
              </p>
            </div>
          </motion.div>

          {/* Top flap — fills the upper half, point sits on the seal */}
          <motion.div
            className="absolute inset-x-0 top-0 z-[3] h-[52%]"
            style={{
              transformOrigin: 'top center',
              clipPath: 'polygon(0 0, 100% 0, 50% 100%)',
              backgroundColor: '#f3e6d4',
              boxShadow: '0 10px 24px rgba(59,46,36,0.12)',
            }}
            animate={{ y: isOpen ? '-105%' : '0%' }}
            transition={{ duration: 0.85, ease: [0.22, 1, 0.36, 1] }}
          />

          {/* Wax seal on the flap point */}
          <div
            className="absolute left-0 right-0 z-[4] flex justify-center"
            style={{ top: 'calc(52% - 2.25rem)' }}
          >
            <motion.div
              animate={{ opacity: isOpen ? 0 : 1, scale: isOpen ? 0.7 : 1 }}
              transition={{ duration: 0.35 }}
            >
              <WaxSeal initials={initials} />
            </motion.div>
          </div>

          <p className="absolute bottom-6 left-0 right-0 z-[5] text-center font-body text-[0.6rem] uppercase tracking-[0.28em] text-moss/50">
            {phase === 'sealed' ? 'Opens in a moment · tap to open' : wedding.displayDate}
          </p>
        </motion.div>
      )}
    </AnimatePresence>
  );
}
