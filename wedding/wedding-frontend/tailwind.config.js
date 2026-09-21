/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        canopy: '#4A1220',
        fern: '#6B1C2C',
        moss: '#8B2E3E',
        sage: '#E8D0CE',
        ivory: '#F7EFE6',
        gold: '#F3E6D4',
        bark: '#3F2428',
      },
      fontFamily: {
        display: ['"Cormorant Garamond"', 'serif'],
        body: ['"Jost"', 'sans-serif'],
      },
      keyframes: {
        drift: {
          '0%': { transform: 'translate(0, 0) rotate(0deg)', opacity: '0' },
          '10%': { opacity: '0.95' },
          '90%': { opacity: '0.8' },
          '100%': { transform: 'translate(var(--drift-x, 40px), 110vh) rotate(var(--drift-r, 90deg))', opacity: '0' },
        },
        glow: {
          '0%, 100%': { opacity: '0.2', transform: 'scale(0.85)' },
          '50%': { opacity: '0.95', transform: 'scale(1.15)' },
        },
        fadeUp: {
          '0%': { opacity: '0', transform: 'translateY(16px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        },
        sway: {
          '0%, 100%': { transform: 'rotate(-6deg)' },
          '50%': { transform: 'rotate(6deg)' },
        },
      },
      animation: {
        drift: 'drift linear infinite',
        glow: 'glow ease-in-out infinite',
        fadeUp: 'fadeUp 0.8s ease-out forwards',
        sway: 'sway 6s ease-in-out infinite',
      },
    },
  },
  plugins: [],
};
