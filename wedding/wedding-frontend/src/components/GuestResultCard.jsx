import { motion } from 'framer-motion';
import Button from './Button.jsx';

const statusLabels = {
  PENDING: 'Awaiting your reply',
  ATTENDING: "You're attending",
  NOT_ATTENDING: "Can't make it",
};

export default function GuestResultCard({ guest, onRespond, busy }) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 12 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.4, ease: 'easeOut' }}
      className="rounded-2xl border border-moss/20 bg-ivory p-4 font-body text-bark shadow-sm sm:p-6"
    >
      <p className="font-display text-2xl italic text-canopy">{guest.name}</p>
      <div className="mt-3 flex flex-wrap gap-x-6 gap-y-1 text-sm text-bark/70">
        {guest.tableNumber != null && <span>Table {guest.tableNumber}</span>}
        {guest.seatNumber != null && <span>Seat {guest.seatNumber}</span>}
        {guest.plusOneCount > 0 && <span>+{guest.plusOneCount} guest(s)</span>}
      </div>
      <p className="mt-3 text-sm uppercase tracking-[0.15em] text-moss">
        {statusLabels[guest.rsvpStatus] ?? guest.rsvpStatus}
      </p>

      <div className="mt-5 grid grid-cols-1 gap-3 sm:grid-cols-2">
        <Button
          variant={guest.rsvpStatus === 'ATTENDING' ? 'primary' : 'outline'}
          disabled={busy}
          onClick={() => onRespond(guest, true)}
          className="w-full whitespace-nowrap px-4 text-xs tracking-[0.12em] sm:text-sm"
        >
          Attending
        </Button>
        <Button
          variant={guest.rsvpStatus === 'NOT_ATTENDING' ? 'primary' : 'outline'}
          disabled={busy}
          onClick={() => onRespond(guest, false)}
          className="w-full whitespace-nowrap px-4 text-xs tracking-[0.12em] sm:text-sm"
        >
          Can't make it
        </Button>
      </div>
    </motion.div>
  );
}
