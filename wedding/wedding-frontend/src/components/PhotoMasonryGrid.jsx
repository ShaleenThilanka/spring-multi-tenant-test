import { motion } from 'framer-motion';

function PhotoTile({ photo, index }) {
  return (
    <motion.figure
      initial={{ opacity: 0, y: 16 }}
      whileInView={{ opacity: 1, y: 0 }}
      viewport={{ once: true, amount: 0.2 }}
      transition={{ duration: 0.45, delay: (index % 8) * 0.04, ease: 'easeOut' }}
      className="mb-3 break-inside-avoid overflow-hidden rounded-xl border border-sage/30 bg-fern/10 sm:mb-4"
    >
      <img src={photo.filePath} alt="" loading="lazy" className="block h-auto w-full" />
    </motion.figure>
  );
}

export default function PhotoMasonryGrid({ photos, emptyLabel = 'No photos yet.' }) {
  if (!photos.length) {
    return <p className="text-center font-body text-bark/60">{emptyLabel}</p>;
  }

  return (
    <div className="columns-2 gap-3 sm:columns-3 sm:gap-4 md:columns-4">
      {photos.map((photo, index) => (
        <PhotoTile key={photo.id ?? photo.filePath} photo={photo} index={index} />
      ))}
    </div>
  );
}
