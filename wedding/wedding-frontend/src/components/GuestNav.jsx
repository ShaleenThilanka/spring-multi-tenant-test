import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { siteConfig } from '../config/siteConfig';
import { fadeUp } from '../lib/motion.js';

export default function GuestNav() {
  const { partnerOne, partnerTwo } = siteConfig.couple;
  return (
    <motion.div
      className="relative z-20 px-5 pt-6 text-center sm:px-10 sm:pt-8"
      variants={fadeUp}
      initial="hidden"
      animate="show"
    >
      <Link
        to="/"
        className="font-display text-xl italic tracking-wide text-canopy no-underline hover:text-moss sm:text-2xl"
      >
        {partnerOne} &amp; {partnerTwo}
      </Link>
    </motion.div>
  );
}
