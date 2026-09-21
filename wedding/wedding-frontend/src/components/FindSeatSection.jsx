import { Link } from 'react-router-dom';
import Button from './Button.jsx';
import GuestResultCard from './GuestResultCard.jsx';
import { useGuestLookup } from '../hooks/useGuestLookup.js';

export default function FindSeatSection() {
  const { name, setName, status, guests, updatingId, search, respondRsvp } = useGuestLookup();

  return (
    <div className="mx-auto max-w-xl text-center">
      <p className="font-body text-xs uppercase tracking-[0.3em] text-moss">Before you arrive</p>
      <h2 className="mt-2 font-display text-4xl italic text-canopy">Find Your Seat</h2>
      <p className="mt-3 font-body text-sm text-bark/70">
        Search your name to see your table and let us know if you'll be joining us.
      </p>

      <form onSubmit={search} className="mt-8 flex flex-col gap-3 sm:flex-row">
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
      </form>

      <div className="mt-8 space-y-6 text-left">
        {status === 'not-found' && (
          <p className="text-center font-body text-bark/70">
            We couldn't find that name — try it exactly as it appears on your invitation.
          </p>
        )}
        {status === 'error' && (
          <p className="text-center font-body text-moss">
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

      <Link
        to="/find-my-seat"
        className="mt-8 inline-block font-body text-xs uppercase tracking-[0.2em] text-moss underline hover:text-fern"
      >
        Open full seat finder
      </Link>
    </div>
  );
}
