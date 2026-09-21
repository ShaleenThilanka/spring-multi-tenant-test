import { useMemo } from 'react';
import RoseBloom from './RoseBloom.jsx';

function seededRoses(count) {
  return Array.from({ length: count }, (_, i) => {
    const left = (i * 37) % 100;
    const size = 36 + ((i * 11) % 14);
    const duration = 18 + ((i * 7) % 14);
    const delay = (i * 2.3) % 20;
    const driftX = (i % 2 === 0 ? 1 : -1) * (30 + ((i * 11) % 60));
    const rotate = 28 + ((i * 29) % 160);
    const isWhite = i % 2 === 0;
    return { left, size, duration, delay, driftX, rotate, isWhite, key: `rose-${i}` };
  });
}

function seededFireflies(count) {
  return Array.from({ length: count }, (_, i) => {
    const left = (i * 23 + 5) % 100;
    const top = 10 + ((i * 17) % 70);
    const size = 3 + ((i * 5) % 4);
    const duration = 2.5 + ((i * 3) % 4);
    const delay = (i * 1.1) % 6;
    return { left, top, size, duration, delay, key: `spark-${i}` };
  });
}

export default function AmbientAtmosphere({ leafCount = 6, fireflyCount = 0, fixed = false }) {
  const roses = useMemo(() => seededRoses(leafCount), [leafCount]);
  const fireflies = useMemo(() => seededFireflies(fireflyCount), [fireflyCount]);

  if (!leafCount && !fireflyCount) return null;

  return (
    <div
      className={
        fixed
          ? 'pointer-events-none fixed inset-0 z-[45] overflow-hidden'
          : 'pointer-events-none absolute inset-0 z-[1] overflow-hidden'
      }
      aria-hidden="true"
    >
      {roses.map((rose) => (
        <RoseBloom
          key={rose.key}
          white={rose.isWhite}
          width={rose.size}
          className="absolute top-[-8%] animate-drift"
          style={{
            left: `${rose.left}%`,
            animationDuration: `${rose.duration}s`,
            animationDelay: `${rose.delay}s`,
            '--drift-x': `${rose.driftX}px`,
            '--drift-r': `${rose.rotate}deg`,
            filter: rose.isWhite
              ? 'drop-shadow(0 1px 2px rgba(180, 35, 58, 0.28))'
              : 'drop-shadow(0 2px 3px rgba(122, 21, 38, 0.22))',
          }}
        />
      ))}

      {fireflies.map((fly) => (
        <span
          key={fly.key}
          className="absolute rounded-full bg-moss animate-glow"
          style={{
            left: `${fly.left}%`,
            top: `${fly.top}%`,
            width: fly.size,
            height: fly.size,
            boxShadow: '0 0 10px 2px rgba(139, 46, 62, 0.35)',
            animationDuration: `${fly.duration}s`,
            animationDelay: `${fly.delay}s`,
          }}
        />
      ))}
    </div>
  );
}
