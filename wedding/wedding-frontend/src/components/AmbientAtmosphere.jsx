import { useMemo } from 'react';
import RosePetal from './RosePetal.jsx';

function seededPetals(count) {
  return Array.from({ length: count }, (_, i) => {
    const left = (i * 37) % 100;
    const size = 16 + ((i * 9) % 12);
    const duration = 14 + ((i * 7) % 12);
    const delay = (i * 1.8) % 14;
    const driftX = (i % 2 === 0 ? 1 : -1) * (24 + ((i * 11) % 50));
    const rotate = 80 + ((i * 47) % 280);
    const isWhite = i % 2 === 0;
    return { left, size, duration, delay, driftX, rotate, isWhite, key: `petal-${i}` };
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

export default function AmbientAtmosphere({ leafCount = 10, fireflyCount = 0, fixed = false }) {
  const petals = useMemo(() => seededPetals(leafCount), [leafCount]);
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
      {petals.map((petal) => (
        <RosePetal
          key={petal.key}
          white={petal.isWhite}
          width={petal.size}
          className="absolute top-[-6%] animate-drift"
          style={{
            left: `${petal.left}%`,
            animationDuration: `${petal.duration}s`,
            animationDelay: `${petal.delay}s`,
            '--drift-x': `${petal.driftX}px`,
            '--drift-r': `${petal.rotate}deg`,
            filter: petal.isWhite
              ? 'drop-shadow(0 1px 1px rgba(180, 35, 58, 0.22))'
              : 'drop-shadow(0 1px 2px rgba(122, 21, 38, 0.18))',
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
