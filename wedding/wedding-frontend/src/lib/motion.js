export const elegantEase = [0.22, 1, 0.36, 1];

export const fadeUp = {
  hidden: { opacity: 0, y: 18 },
  show: {
    opacity: 1,
    y: 0,
    transition: { duration: 0.85, ease: elegantEase },
  },
};

export const stagger = {
  hidden: {},
  show: {
    transition: { staggerChildren: 0.16, delayChildren: 0.08 },
  },
};
