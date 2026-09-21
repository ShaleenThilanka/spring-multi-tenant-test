import { useEffect, useMemo, useRef, useState } from 'react';
import Button from '../../components/Button.jsx';
import AdminNav from '../../components/AdminNav.jsx';
import {
  adminDeletePhoto,
  adminUploadPreshootPhotos,
  apiMessage,
  getGuestUploadsGallery,
  getPreshootGallery,
} from '../../api/client.js';
import { downloadImage } from '../../lib/download.js';

const TABS = [
  { key: 'preshoot', label: 'Pre-Shoot' },
  { key: 'guest', label: 'Guest uploads' },
];

function PhotoGrid({ photos, removingId, onView, onRemove, emptyLabel, showGuestName }) {
  if (!photos.length) {
    return <p className="mt-4 font-body text-sm text-bark/50">{emptyLabel}</p>;
  }

  return (
    <ul className="mt-4 grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-4">
      {photos.map((photo) => (
        <li key={photo.id} className="overflow-hidden rounded-xl border border-sage/30 bg-ivory">
          <button
            type="button"
            onClick={() => onView(photo)}
            className="block w-full"
            aria-label="View photo"
          >
            <img src={photo.filePath} alt="" className="aspect-square h-full w-full object-cover" />
          </button>
          {showGuestName && photo.guestName && (
            <p className="truncate px-3 pt-2 font-body text-xs text-bark/70">{photo.guestName}</p>
          )}
          <div className="flex items-center justify-between gap-2 px-3 py-2">
            <button
              type="button"
              onClick={() => onView(photo)}
              className="font-body text-xs uppercase tracking-[0.12em] text-moss hover:underline"
            >
              View
            </button>
            <button
              type="button"
              onClick={() => onRemove(photo)}
              disabled={removingId === photo.id}
              className="font-body text-xs uppercase tracking-[0.12em] text-red-700 hover:underline disabled:opacity-50"
            >
              {removingId === photo.id ? 'Removing…' : 'Remove'}
            </button>
          </div>
        </li>
      ))}
    </ul>
  );
}

export default function AdminPreshootPhotos() {
  const [tab, setTab] = useState('preshoot');
  const [files, setFiles] = useState([]);
  const [preshoot, setPreshoot] = useState([]);
  const [guestPhotos, setGuestPhotos] = useState([]);
  const [status, setStatus] = useState('idle');
  const [error, setError] = useState('');
  const [removingId, setRemovingId] = useState(null);
  const [viewing, setViewing] = useState(null);
  const [guestFilter, setGuestFilter] = useState('all');
  const fileInputRef = useRef(null);

  const load = async () => {
    const [pre, guest] = await Promise.all([getPreshootGallery(), getGuestUploadsGallery()]);
    setPreshoot(pre);
    setGuestPhotos(guest);
  };

  useEffect(() => {
    load().catch((err) => {
      setError(apiMessage(err, 'Could not load photos. Start the wedding service on port 7001.'));
    });
  }, []);

  useEffect(() => {
    if (!viewing) return undefined;
    const onKey = (e) => e.key === 'Escape' && setViewing(null);
    window.addEventListener('keydown', onKey);
    return () => window.removeEventListener('keydown', onKey);
  }, [viewing]);

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!files.length) return;
    setStatus('loading');
    setError('');
    try {
      await adminUploadPreshootPhotos(files);
      setStatus('done');
      setFiles([]);
      if (fileInputRef.current) fileInputRef.current.value = '';
      await load();
    } catch (err) {
      setStatus('error');
      setError(apiMessage(err, 'Upload failed. Please try again.'));
    }
  };

  const handleRemove = async (photo) => {
    if (!window.confirm('Remove this photo from the gallery?')) return;
    setRemovingId(photo.id);
    setError('');
    try {
      await adminDeletePhoto(photo.id);
      if (viewing?.id === photo.id) setViewing(null);
      await load();
    } catch (err) {
      setError(apiMessage(err, 'Could not remove this photo.'));
    } finally {
      setRemovingId(null);
    }
  };

  const guestNames = useMemo(() => {
    const names = [...new Set(guestPhotos.map((photo) => photo.guestName).filter(Boolean))];
    names.sort((a, b) => a.localeCompare(b));
    return names;
  }, [guestPhotos]);

  const visiblePhotos =
    tab === 'preshoot'
      ? preshoot
      : guestFilter === 'all'
        ? guestPhotos
        : guestPhotos.filter((photo) => photo.guestName === guestFilter);

  return (
    <main className="min-h-screen bg-ivory px-6 py-12 sm:px-10" style={{ colorScheme: 'light' }}>
      <div className="mx-auto max-w-5xl">
        <AdminNav />
        <p className="font-body text-xs uppercase tracking-[0.3em] text-moss">Admin</p>
        <h1 className="mt-1 font-display text-3xl italic text-bark">Photos</h1>
        <p className="mt-2 font-body text-sm text-bark/70">
          Preview pre-shoot and guest uploads, then remove anything that should not stay in the gallery.
        </p>

        <div className="mt-6 flex gap-3">
          {TABS.map((item) => (
            <button
              key={item.key}
              type="button"
              onClick={() => setTab(item.key)}
              className={`rounded-full px-5 py-2 font-body text-sm uppercase tracking-[0.15em] transition-colors ${
                tab === item.key ? 'bg-moss text-ivory' : 'bg-sage/20 text-bark/70 hover:bg-sage/30'
              }`}
              aria-pressed={tab === item.key}
            >
              {item.label}
            </button>
          ))}
        </div>

        {tab === 'preshoot' && (
          <form onSubmit={handleUpload} className="mt-6 flex flex-col gap-4 rounded-xl border border-sage/30 bg-sage/10 p-6">
            <input
              ref={fileInputRef}
              type="file"
              accept="image/*"
              multiple
              onChange={(e) => setFiles(Array.from(e.target.files ?? []))}
              className="font-body text-sm text-bark"
            />
            {files.length > 0 && (
              <p className="font-body text-sm text-bark/60">{files.length} file(s) selected</p>
            )}
            <Button type="submit" variant="ghost" disabled={!files.length || status === 'loading'}>
              {status === 'loading' ? 'Uploading…' : 'Upload batch'}
            </Button>
            {status === 'done' && <p className="font-body text-sm text-moss">Photos uploaded.</p>}
          </form>
        )}

        {error && <p className="mt-4 font-body text-sm text-red-700">{error}</p>}

        <h2 className="mt-10 font-display text-2xl italic text-bark">
          {tab === 'preshoot' ? 'Pre-shoot images' : 'Guest uploads'}
        </h2>
        {tab === 'guest' && (
          <div className="mt-3 max-w-xs">
            <label htmlFor="guest-photo-filter" className="block font-body text-xs uppercase tracking-wide text-bark/60">
              Filter by guest
            </label>
            <select
              id="guest-photo-filter"
              value={guestFilter}
              onChange={(e) => setGuestFilter(e.target.value)}
              className="field w-full"
            >
              <option value="all">All guests</option>
              {guestNames.map((name) => (
                <option key={name} value={name}>
                  {name}
                </option>
              ))}
            </select>
          </div>
        )}
        <PhotoGrid
          photos={visiblePhotos}
          removingId={removingId}
          onView={setViewing}
          onRemove={handleRemove}
          showGuestName={tab === 'guest'}
          emptyLabel={
            tab === 'preshoot' ? 'No pre-shoot photos uploaded yet.' : 'No guest photos uploaded yet.'
          }
        />
      </div>

      {viewing && (
        <div
          className="fixed inset-0 z-50 flex items-center justify-center bg-canopy/80 px-4 py-8"
          onClick={() => setViewing(null)}
          role="dialog"
          aria-modal="true"
          aria-label="Photo preview"
        >
          <div className="relative max-h-full w-full max-w-3xl" onClick={(e) => e.stopPropagation()}>
            <img src={viewing.filePath} alt="" className="max-h-[80vh] w-full rounded-xl object-contain" />
            {viewing.guestName && (
              <p className="mt-3 text-center font-display text-xl italic text-ivory">{viewing.guestName}</p>
            )}
            <div className="mt-4 flex justify-end gap-4">
              <button
                type="button"
                onClick={() => downloadImage(viewing.filePath, viewing.guestName || 'photo')}
                className="font-body text-xs uppercase tracking-[0.15em] text-ivory hover:text-gold"
              >
                Download
              </button>
              <button
                type="button"
                onClick={() => handleRemove(viewing)}
                disabled={removingId === viewing.id}
                className="font-body text-xs uppercase tracking-[0.15em] text-red-200 hover:underline disabled:opacity-50"
              >
                Remove
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
