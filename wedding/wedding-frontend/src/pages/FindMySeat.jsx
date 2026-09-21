import { motion } from 'framer-motion';
import PageShell from '../components/PageShell.jsx';
import GuestNav from '../components/GuestNav.jsx';
import VineDivider from '../components/VineDivider.jsx';
import Button from '../components/Button.jsx';
import GuestResultCard from '../components/GuestResultCard.jsx';
import NameFlourish from '../components/NameFlourish.jsx';
import { useGuestLookup } from '../hooks/useGuestLookup.js';
import { fadeUp, stagger } from '../lib/motion.js';

export default function FindMySeat() {
  const { name, setName, status, guests, updatingId, search, respondRsvp } = useGuestLookup();

  return (
    <PageShell className="bg-ivory">
      <GuestNav />

      <motion.section
        className="mx-auto max-w-xl px-5 pb-24 pt-2 text-center sm:px-10"
        variants={stagger}
        initial="hidden"
        animate="show"
      >
        <motion.p variants={fadeUp} className="font-body text-[0.65rem] uppercase tracking-[0.3em] text-moss">
          Find your place
        </motion.p>
        <motion.h1 variants={fadeUp} className="mt-2 font-display text-4xl italic text-canopy">
          Find My Seat
        </motion.h1>
        <NameFlourish className="mx-auto mt-2 h-8 w-48" />
        <motion.p variants={fadeUp} className="mt-3 font-body text-sm text-bark/70">
          Search by the name your invitation was addressed to.
        </motion.p>

        <motion.form variants={fadeUp} onSubmit={search} className="mt-8 flex flex-col gap-3 sm:flex-row">
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Your full name"
            aria-label="Your full name"
            className="field mt-0 flex-1 rounded-full px-5 py-3"
          />
          <Button type="submit" disabled={status === 'loading'}>
            {status === 'loading' ? 'Searching…' : 'Search'}
          </Button>
        </motion.form>

        <div className="mt-10 space-y-6 text-left">
          {status === 'not-found' && (
            <p className="font-body text-bark/70">
              We couldn't find that name. Try the name exactly as it appears on your invitation.
            </p>
          )}
          {status === 'error' && (
            <p className="font-body text-moss">
              Something went wrong reaching the guest list. Please try again shortly.
            </p>
          )}
          {guests.map((guest) => (
            <GuestResultCard
              key={guest.id}
              guest={guest}
              busy={updatingId === guest.id}
              onRespond={respondRsvp}
            />
          ))}
        </div>
      </motion.section>

      <VineDivider className="bg-ivory" />
    </PageShell>
  );
}
