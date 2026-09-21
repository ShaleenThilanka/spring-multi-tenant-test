import { motion } from 'framer-motion';

export default function ScrollCue({ targetId }) {
  const handleClick = () => {
    document.getElementById(targetId)?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <button
      onClick={handleClick}
      aria-label="Scroll to next section"
      className="group flex flex-col items-center gap-2 text-moss/70 transition-colors hover:text-fern"
    >
      <span className="font-body text-[10px] uppercase tracking-[0.3em]">Scroll</span>
      <motion.svg
        width="18"
        height="26"
        viewBox="0 0 18 26"
        fill="none"
        animate={{ y: [0, 6, 0] }}
        transition={{ duration: 1.8, repeat: Infinity, ease: 'easeInOut' }}
      >
        <rect x="1" y="1" width="16" height="24" rx="8" stroke="currentColor" strokeWidth="1.2" />
        <circle cx="9" cy="8" r="2" fill="currentColor" />
      </motion.svg>
    </button>
  );
}
