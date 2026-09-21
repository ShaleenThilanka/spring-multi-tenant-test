import { useEffect, useState } from 'react';
import { AnimatePresence, motion } from 'framer-motion';
import { siteConfig } from '../config/siteConfig';

function getTimeLeft() {
  const diff = new Date(siteConfig.countdownTarget).getTime() - Date.now();
  const clamped = Math.max(diff, 0);
  return {
    total: clamped,
    days: Math.floor(clamped / (1000 * 60 * 60 * 24)),
    hours: Math.floor((clamped / (1000 * 60 * 60)) % 24),
    minutes: Math.floor((clamped / (1000 * 60)) % 60),
    seconds: Math.floor((clamped / 1000) % 60),
  };
}

function Unit({ value, label }) {
  return (
    <div className="flex flex-col items-center">
      <div className="relative h-14 w-14 overflow-hidden rounded-lg border border-moss/20 bg-ivory sm:h-16 sm:w-16">
        <AnimatePresence mode="popLayout">
          <motion.span
            key={value}
            initial={{ y: '100%', opacity: 0 }}
            animate={{ y: '0%', opacity: 1 }}
            exit={{ y: '-100%', opacity: 0 }}
            transition={{ duration: 0.4, ease: 'easeOut' }}
            className="absolute inset-0 flex items-center justify-center font-display text-2xl italic text-canopy sm:text-3xl"
          >
            {String(value).padStart(2, '0')}
          </motion.span>
        </AnimatePresence>
      </div>
      <span className="mt-2 font-body text-[10px] uppercase tracking-[0.25em] text-moss">{label}</span>
    </div>
  );
}

export default function CountdownTimer() {
  const [time, setTime] = useState(getTimeLeft);

  useEffect(() => {
    const id = setInterval(() => setTime(getTimeLeft()), 1000);
    return () => clearInterval(id);
  }, []);

  if (time.total <= 0) {
    return (
      <p className="font-display text-2xl italic text-moss">Today is the day 🎉</p>
    );
  }

  return (
    <div className="flex items-center gap-3 sm:gap-5">
      <Unit value={time.days} label="Days" />
      <span className="pb-6 font-display text-2xl text-moss/50">:</span>
      <Unit value={time.hours} label="Hours" />
      <span className="pb-6 font-display text-2xl text-moss/50">:</span>
      <Unit value={time.minutes} label="Mins" />
      <span className="pb-6 font-display text-2xl text-moss/50">:</span>
      <Unit value={time.seconds} label="Secs" />
    </div>
  );
}
