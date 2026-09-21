import { RoseMark } from './RoseBloom.jsx';

export default function VineDivider({ className = '' }) {
  return (
    <svg
      className={`vine-divider ${className}`}
      viewBox="0 0 600 48"
      preserveAspectRatio="xMidYMid meet"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      aria-hidden="true"
    >
      <path
        d="M0 24 C 80 10, 140 38, 220 24 S 340 10, 380 24 S 500 38, 600 24"
        stroke="#5F8A64"
        strokeWidth="1.4"
        strokeLinecap="round"
      />
      <RoseMark x={220} y={22} scale={0.85} />
      <RoseMark x={380} y={26} scale={0.85} white rotate={8} />
    </svg>
  );
}
