import { useEffect, useMemo, useRef, useState } from 'react';
import Button from '../../components/Button.jsx';
import AdminNav from '../../components/AdminNav.jsx';
import {
  adminAssignTable,
  adminCreateGuest,
  adminListGuests,
  adminListTables,
  apiMessage,
  getGuestUploadsGallery,
  uploadGuestPhoto,
} from '../../api/client.js';
import { downloadCsv, downloadImage } from '../../lib/download.js';

const statusLabels = {
  PENDING: 'Pending',
  ATTENDING: 'Attending',
  NOT_ATTENDING: 'Not attending',
};

const RSVP_FILTERS = [
  { key: 'ALL', label: 'All guests' },
  { key: 'ATTENDING', label: 'Attending' },
  { key: 'NOT_ATTENDING', label: 'Not attending' },
  { key: 'PENDING', label: 'Pending' },
];

function partySize(guest) {
  return 1 + (Number(guest.plusOneCount) || 0);
}

export default function AdminGuests() {
  const [guests, setGuests] = useState([]);
  const [tables, setTables] = useState([]);
  const [photos, setPhotos] = useState([]);
  const [form, setForm] = useState({ name: '', plusOneCount: '0' });
  const [query, setQuery] = useState('');
  const [rsvpFilter, setRsvpFilter] = useState('ALL');
  const [error, setError] = useState('');
  const [busyGuestId, setBusyGuestId] = useState(null);
  const [uploadingId, setUploadingId] = useState(null);
  const [viewing, setViewing] = useState(null);
  const fileInputs = useRef({});

  const load = async () => {
    const [g, t, p] = await Promise.all([
      adminListGuests(),
      adminListTables(),
      getGuestUploadsGallery().catch(() => []),
    ]);
    setGuests(g);
    setTables(t);
    setPhotos(p);
  };

  useEffect(() => {
    load().catch((err) => {
      setError(apiMessage(err, 'Could not load guests. Start the wedding service on port 7001.'));
    });
  }, []);

  useEffect(() => {
    if (!viewing) return undefined;
    const onKey = (e) => e.key === 'Escape' && setViewing(null);
    window.addEventListener('keydown', onKey);
    return () => window.removeEventListener('keydown', onKey);
  }, [viewing]);

  const occupancy = useMemo(() => {
    const map = {};
    for (const t of tables) map[t.id] = 0;
    for (const g of guests) {
      if (g.tableId != null) {
        map[g.tableId] = (map[g.tableId] ?? 0) + partySize(g);
      }
    }
    return map;
  }, [guests, tables]);

  const photosByGuest = useMemo(() => {
    const map = {};
    for (const photo of photos) {
      const id = photo.guestId;
      if (id == null) continue;
      if (!map[id]) map[id] = [];
      map[id].push(photo);
    }
    return map;
  }, [photos]);

  const counts = useMemo(() => {
    const attending = guests.filter((g) => g.rsvpStatus === 'ATTENDING');
    const notAttending = guests.filter((g) => g.rsvpStatus === 'NOT_ATTENDING');
    const pending = guests.filter((g) => g.rsvpStatus === 'PENDING' || !g.rsvpStatus);
    return {
      ALL: { guests: guests.length, people: guests.reduce((sum, g) => sum + partySize(g), 0) },
      ATTENDING: { guests: attending.length, people: attending.reduce((sum, g) => sum + partySize(g), 0) },
      NOT_ATTENDING: { guests: notAttending.length, people: notAttending.reduce((sum, g) => sum + partySize(g), 0) },
      PENDING: { guests: pending.length, people: pending.reduce((sum, g) => sum + partySize(g), 0) },
    };
  }, [guests]);

  const filteredGuests = useMemo(() => {
    const q = query.trim().toLowerCase();
    return guests.filter((guest) => {
      const matchesName = !q || guest.name?.toLowerCase().includes(q);
      const matchesRsvp = rsvpFilter === 'ALL' || guest.rsvpStatus === rsvpFilter;
      return matchesName && matchesRsvp;
    });
  }, [guests, query, rsvpFilter]);

  const handleAddGuest = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await adminCreateGuest({
        name: form.name.trim(),
        plusOneCount: Math.max(0, Number(form.plusOneCount) || 0),
      });
      setForm({ name: '', plusOneCount: '0' });
      await load();
    } catch (err) {
      setError(apiMessage(err, 'Could not add guest.'));
    }
  };

  const handleAssign = async (guest, tableId) => {
    setError('');
    setBusyGuestId(guest.id);
    try {
      if (tableId) await adminAssignTable(guest.id, Number(tableId));
      await load();
    } catch (err) {
      setError(apiMessage(err, `Could not seat ${guest.name} — check table capacity.`));
    } finally {
      setBusyGuestId(null);
    }
  };

  const handleUpload = async (guest, file) => {
    if (!file) return;
    setUploadingId(guest.id);
    setError('');
    try {
      await uploadGuestPhoto(guest.id, file);
      await load();
    } catch (err) {
      setError(apiMessage(err, `Could not upload a photo for ${guest.name}.`));
    } finally {
      setUploadingId(null);
      if (fileInputs.current[guest.id]) fileInputs.current[guest.id].value = '';
    }
  };

  const handleDownloadList = () => {
    downloadCsv(
      'wedding-guests.csv',
      ['Name', 'Plus-ones', 'RSVP', 'Table'],
      filteredGuests.map((guest) => [
        guest.name,
        guest.plusOneCount ?? 0,
        statusLabels[guest.rsvpStatus] ?? guest.rsvpStatus ?? '',
        guest.tableNumber ?? '',
      ])
    );
  };

  const handleDownloadPhotos = async (guest) => {
    const list = photosByGuest[guest.id] ?? [];
    if (!list.length) return;
    setError('');
    try {
      for (const [index, photo] of list.entries()) {
        const safeName = guest.name.replace(/[^\w\s-]/g, '').trim().replace(/\s+/g, '-').toLowerCase();
        await downloadImage(photo.filePath, `${safeName}-photo-${index + 1}`);
      }
    } catch (err) {
      setError(apiMessage(err, `Could not download photos for ${guest.name}.`));
    }
  };

  return (
    <main className="min-h-screen bg-ivory px-6 py-12 sm:px-10" style={{ colorScheme: 'light' }}>
      <div className="mx-auto max-w-6xl">
        <AdminNav />
        <p className="font-body text-xs uppercase tracking-[0.3em] text-moss">Admin</p>
        <h1 className="mt-1 font-display text-3xl italic text-bark">Guests</h1>

        <div className="mt-6 grid grid-cols-2 gap-3 sm:grid-cols-4">
          {RSVP_FILTERS.map((item) => {
            const count = counts[item.key];
            const active = rsvpFilter === item.key;
            return (
              <button
                key={item.key}
                type="button"
                onClick={() => setRsvpFilter(item.key)}
                className={`rounded-xl border px-4 py-3 text-left transition-colors ${
                  active ? 'border-moss bg-moss text-ivory' : 'border-sage/30 bg-ivory text-bark hover:border-moss/50'
                }`}
              >
                <p className={`font-body text-[0.65rem] uppercase tracking-[0.18em] ${active ? 'text-ivory/80' : 'text-bark/50'}`}>
                  {item.label}
                </p>
                <p className="mt-1 font-display text-3xl italic">{count.guests}</p>
                <p className={`font-body text-xs ${active ? 'text-ivory/70' : 'text-bark/50'}`}>
                  {count.people} with plus-ones
                </p>
              </button>
            );
          })}
        </div>

        <form onSubmit={handleAddGuest} className="mt-6 flex flex-wrap items-end gap-3 rounded-xl border border-sage/30 bg-sage/10 p-4">
          <div className="min-w-[12rem] flex-1">
            <label htmlFor="guest-name" className="block font-body text-xs uppercase tracking-wide text-bark/60">
              Name
            </label>
            <input
              id="guest-name"
              type="text"
              autoComplete="off"
              placeholder="Guest full name"
              value={form.name}
              onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))}
              required
              className="field w-full"
            />
          </div>
          <div>
            <label htmlFor="guest-plus" className="block font-body text-xs uppercase tracking-wide text-bark/60">
              Plus-ones
            </label>
            <input
              id="guest-plus"
              type="number"
              min="0"
              step="1"
              inputMode="numeric"
              value={form.plusOneCount}
              onChange={(e) => setForm((f) => ({ ...f, plusOneCount: e.target.value }))}
              className="field w-24"
            />
          </div>
          <Button type="submit" variant="ghost">Add guest</Button>
        </form>

        <div className="mt-6 flex flex-wrap items-end gap-3">
          <div className="min-w-[12rem] flex-1">
            <label htmlFor="guest-filter" className="block font-body text-xs uppercase tracking-wide text-bark/60">
              Filter by guest
            </label>
            <input
              id="guest-filter"
              type="text"
              autoComplete="off"
              placeholder="Search name"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              className="field w-full"
            />
          </div>
          <Button type="button" variant="ghost" onClick={handleDownloadList}>
            Download list
          </Button>
        </div>

        {error && <p className="mt-4 font-body text-sm text-red-700">{error}</p>}

        <div className="mt-6 overflow-x-auto rounded-xl border border-sage/30 bg-ivory">
          <table className="w-full min-w-[52rem] font-body text-sm text-bark">
            <thead className="bg-sage/20 text-left uppercase tracking-wide text-bark/70">
              <tr>
                <th className="px-4 py-3">Name</th>
                <th className="px-4 py-3">Plus-ones</th>
                <th className="px-4 py-3">RSVP</th>
                <th className="px-4 py-3">Table</th>
                <th className="px-4 py-3">Photos</th>
              </tr>
            </thead>
            <tbody>
              {filteredGuests.map((guest) => {
                const guestPhotos = photosByGuest[guest.id] ?? [];
                return (
                  <tr key={guest.id} className="border-t border-sage/20 align-top">
                    <td className="px-4 py-3">{guest.name}</td>
                    <td className="px-4 py-3">{guest.plusOneCount}</td>
                    <td className="px-4 py-3">{statusLabels[guest.rsvpStatus] ?? guest.rsvpStatus}</td>
                    <td className="px-4 py-3">
                      <select
                        value={guest.tableId ?? ''}
                        disabled={busyGuestId === guest.id}
                        onChange={(e) => handleAssign(guest, e.target.value || null)}
                        className="field mt-0"
                      >
                        <option value="">Unassigned</option>
                        {tables.map((table) => {
                          const occupied = occupancy[table.id] ?? 0;
                          const ownSeats = guest.tableId === table.id ? partySize(guest) : 0;
                          const available = table.capacity - occupied + ownSeats;
                          return (
                            <option key={table.id} value={table.id}>
                              Table {table.tableNumber} — {available} seat(s) left
                            </option>
                          );
                        })}
                      </select>
                    </td>
                    <td className="px-4 py-3">
                      <div className="flex flex-wrap items-center gap-2">
                        {guestPhotos.slice(0, 3).map((photo) => (
                          <button
                            key={photo.id}
                            type="button"
                            onClick={() => setViewing({ ...photo, guestName: guest.name })}
                            className="h-12 w-12 overflow-hidden rounded-md border border-sage/30"
                          >
                            <img src={photo.filePath} alt="" className="h-full w-full object-cover" />
                          </button>
                        ))}
                        {guestPhotos.length > 3 && (
                          <span className="font-body text-xs text-bark/50">+{guestPhotos.length - 3}</span>
                        )}
                      </div>
                      <div className="mt-2 flex flex-wrap gap-3">
                        <label className="cursor-pointer font-body text-xs uppercase tracking-[0.12em] text-moss hover:underline">
                          {uploadingId === guest.id ? 'Uploading…' : 'Upload'}
                          <input
                            ref={(node) => {
                              fileInputs.current[guest.id] = node;
                            }}
                            type="file"
                            accept="image/*"
                            className="hidden"
                            disabled={uploadingId === guest.id}
                            onChange={(e) => handleUpload(guest, e.target.files?.[0])}
                          />
                        </label>
                        {guestPhotos.length > 0 && (
                          <button
                            type="button"
                            onClick={() => handleDownloadPhotos(guest)}
                            className="font-body text-xs uppercase tracking-[0.12em] text-moss hover:underline"
                          >
                            Download
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                );
              })}
              {!filteredGuests.length && (
                <tr>
                  <td colSpan={5} className="px-4 py-6 text-center text-bark/50">
                    {guests.length ? 'No guests match this filter.' : 'No guests yet.'}
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {viewing && (
        <div
          className="fixed inset-0 z-50 flex items-center justify-center bg-canopy/80 px-4 py-8"
          onClick={() => setViewing(null)}
          role="dialog"
          aria-modal="true"
          aria-label="Guest photo preview"
        >
          <div className="relative max-h-full w-full max-w-3xl" onClick={(e) => e.stopPropagation()}>
            <img src={viewing.filePath} alt="" className="max-h-[80vh] w-full rounded-xl object-contain" />
            {viewing.guestName && (
              <p className="mt-3 text-center font-display text-xl italic text-ivory">{viewing.guestName}</p>
            )}
            <div className="mt-4 flex justify-end gap-4">
              <button
                type="button"
                onClick={() => downloadImage(viewing.filePath, viewing.guestName || 'guest-photo')}
                className="font-body text-xs uppercase tracking-[0.15em] text-ivory hover:text-gold"
              >
                Download
              </button>
              <button
                type="button"
                onClick={() => setViewing(null)}
                className="font-body text-xs uppercase tracking-[0.15em] text-ivory hover:text-gold"
              >
                Close
              </button>
            </div>
          </div>
        </div>
      )}
    </main>
  );
}
