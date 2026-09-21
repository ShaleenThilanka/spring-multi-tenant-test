import { motion } from 'framer-motion';
import { elegantEase } from '../lib/motion.js';

export default function Reveal({ children, className = '', delay = 0, y = 20 }) {
  return (
    <motion.div
      className={className}
      initial={{ opacity: 0, y }}
      whileInView={{ opacity: 1, y: 0 }}
      viewport={{ once: true, amount: 0.22 }}
      transition={{ duration: 0.8, delay, ease: elegantEase }}
    >
      {children}
    </motion.div>
  );
}
