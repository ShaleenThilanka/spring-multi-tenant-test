import { PetalMark } from './RosePetal.jsx';

export default function VineDivider({ className = '' }) {
  return (
    <svg
      className={`vine-divider ${className}`}
      viewBox="0 0 600 40"
      preserveAspectRatio="xMidYMid meet"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      aria-hidden="true"
    >
      <path
        d="M0 20 C 80 8, 140 32, 220 20 S 340 8, 380 20 S 500 32, 600 20"
        stroke="#E8D0CE"
        strokeWidth="1.2"
        strokeLinecap="round"
      />
      <PetalMark x={220} y={18} scale={0.7} rotate={-18} />
      <PetalMark x={380} y={22} scale={0.7} white rotate={22} />
    </svg>
  );
}
