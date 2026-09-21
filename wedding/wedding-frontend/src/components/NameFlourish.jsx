import { motion } from 'framer-motion';
import { elegantEase } from '../lib/motion.js';
import { PetalMark } from './RosePetal.jsx';

export default function NameFlourish({ className = '' }) {
  return (
    <motion.svg
      className={className}
      viewBox="0 0 220 32"
      fill="none"
      aria-hidden="true"
      initial="hidden"
      animate="show"
    >
      <motion.path
        d="M8 18 C 40 8, 70 26, 110 16 S 170 8, 212 18"
        stroke="#E8D0CE"
        strokeWidth="1.15"
        strokeLinecap="round"
        variants={{
          hidden: { pathLength: 0, opacity: 0 },
          show: {
            pathLength: 1,
            opacity: 0.95,
            transition: { duration: 1.4, delay: 0.45, ease: elegantEase },
          },
        }}
      />
      <motion.g
        initial={{ opacity: 0, scale: 0.4 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5, delay: 0.9, ease: elegantEase }}
        style={{ transformOrigin: '78px 16px' }}
      >
        <PetalMark x={78} y={16} scale={0.72} rotate={-16} />
      </motion.g>
      <motion.g
        initial={{ opacity: 0, scale: 0.4 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5, delay: 1.05, ease: elegantEase }}
        style={{ transformOrigin: '142px 16px' }}
      >
        <PetalMark x={142} y={16} scale={0.72} white rotate={18} />
      </motion.g>
    </motion.svg>
  );
}
