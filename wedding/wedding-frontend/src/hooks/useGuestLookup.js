import { useState } from 'react';
import { searchGuestByName, updateRsvp } from '../api/client.js';

export function useGuestLookup() {
  const [name, setName] = useState('');
  const [status, setStatus] = useState('idle'); // idle | loading | found | not-found | error
  const [guests, setGuests] = useState([]);
  const [updatingId, setUpdatingId] = useState(null);

  const search = async (e) => {
    e?.preventDefault?.();
    if (!name.trim()) return;
    setStatus('loading');
    try {
      const results = await searchGuestByName(name.trim());
      const list = Array.isArray(results) ? results : results ? [results] : [];
      setGuests(list);
      setStatus(list.length ? 'found' : 'not-found');
    } catch {
      setStatus('error');
    }
  };

  const respondRsvp = async (guest, attending) => {
    setUpdatingId(guest.id);
    try {
      await updateRsvp(guest.id, attending);
      setGuests((prev) =>
        prev.map((g) =>
          g.id === guest.id ? { ...g, rsvpStatus: attending ? 'ATTENDING' : 'NOT_ATTENDING' } : g
        )
      );
    } catch {
      setStatus('error');
    } finally {
      setUpdatingId(null);
    }
  };

  return { name, setName, status, guests, updatingId, search, respondRsvp };
}
