import { motion } from 'framer-motion';
import { elegantEase } from '../lib/motion.js';
import { RoseMark } from './RoseBloom.jsx';

export default function NameFlourish({ className = '' }) {
  return (
    <motion.svg
      className={className}
      viewBox="0 0 220 36"
      fill="none"
      aria-hidden="true"
      initial="hidden"
      animate="show"
    >
      <motion.path
        d="M8 20 C 40 10, 70 28, 110 18 S 170 10, 212 20"
        stroke="#5F8A64"
        strokeWidth="1.2"
        strokeLinecap="round"
        variants={{
          hidden: { pathLength: 0, opacity: 0 },
          show: {
            pathLength: 1,
            opacity: 0.9,
            transition: { duration: 1.4, delay: 0.45, ease: elegantEase },
          },
        }}
      />
      {[70, 150].map((x, i) => (
        <motion.g
          key={x}
          initial={{ opacity: 0, scale: 0.4 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 0.5, delay: 0.85 + i * 0.12, ease: elegantEase }}
          style={{ transformOrigin: `${x}px 18px` }}
        >
          <RoseMark x={x} y={18} scale={0.9} white={i === 1} rotate={i === 0 ? -8 : 10} />
        </motion.g>
      ))}
    </motion.svg>
  );
}
