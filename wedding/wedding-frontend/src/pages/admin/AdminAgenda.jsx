import { useEffect, useState } from 'react';
import Button from '../../components/Button.jsx';
import AdminNav from '../../components/AdminNav.jsx';
import { adminCreateAgendaItem, adminDeleteAgendaItem, apiMessage, getAgenda } from '../../api/client.js';
import { siteConfig } from '../../config/siteConfig';

export default function AdminAgenda() {
  const [items, setItems] = useState([]);
  const [form, setForm] = useState({ time: '', title: '', description: '' });
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  const load = async () => {
    const data = await getAgenda();
    setItems(data);
  };

  useEffect(() => {
    load().catch((err) => {
      setError(apiMessage(err, 'Could not load agenda. Start the wedding service on port 7001.'));
    });
  }, []);

  const handleAdd = async (e) => {
    e.preventDefault();
    setError('');
    setBusy(true);
    try {
      await adminCreateAgendaItem({
        time: form.time.trim(),
        title: form.title.trim(),
        description: form.description.trim(),
      });
      setForm({ time: '', title: '', description: '' });
      await load();
    } catch (err) {
      setError(apiMessage(err, 'Could not save agenda item.'));
    } finally {
      setBusy(false);
    }
  };

  const handleDelete = async (id) => {
    setError('');
    try {
      await adminDeleteAgendaItem(id);
      await load();
    } catch (err) {
      setError(apiMessage(err, 'Could not delete agenda item.'));
    }
  };

  const loadDefaults = async () => {
    setError('');
    setBusy(true);
    try {
      for (const item of siteConfig.agenda) {
        await adminCreateAgendaItem(item);
      }
      await load();
    } catch (err) {
      setError(apiMessage(err, 'Could not load default agenda.'));
    } finally {
      setBusy(false);
    }
  };

  return (
    <main className="min-h-screen bg-ivory px-6 py-12 sm:px-10" style={{ colorScheme: 'light' }}>
      <div className="mx-auto max-w-3xl">
        <AdminNav />
        <p className="font-body text-xs uppercase tracking-[0.3em] text-moss">Admin</p>
        <h1 className="mt-1 font-display text-3xl italic text-bark">Agenda</h1>
        <p className="mt-2 font-body text-sm text-bark/70">
          Add the day's times, titles, and short descriptions. These appear in View Agenda on the invitation.
        </p>

        <form onSubmit={handleAdd} className="mt-6 flex flex-col gap-3 rounded-xl border border-sage/30 bg-sage/10 p-4">
          <div className="grid gap-3 sm:grid-cols-2">
            <div>
              <label htmlFor="agenda-time" className="block font-body text-xs uppercase tracking-wide text-bark/60">
                Time
              </label>
              <input
                id="agenda-time"
                type="text"
                autoComplete="off"
                placeholder="9:30 AM"
                value={form.time}
                onChange={(e) => setForm((f) => ({ ...f, time: e.target.value }))}
                required
                maxLength={40}
                className="field w-full"
              />
            </div>
            <div>
              <label htmlFor="agenda-title" className="block font-body text-xs uppercase tracking-wide text-bark/60">
                Small title
              </label>
              <input
                id="agenda-title"
                type="text"
                autoComplete="off"
                placeholder="Poruwa Ceremony"
                value={form.title}
                onChange={(e) => setForm((f) => ({ ...f, title: e.target.value }))}
                required
                maxLength={80}
                className="field w-full"
              />
            </div>
          </div>
          <div>
            <label htmlFor="agenda-description" className="block font-body text-xs uppercase tracking-wide text-bark/60">
              Short description
            </label>
            <textarea
              id="agenda-description"
              rows={3}
              placeholder="A few words about this moment."
              value={form.description}
              onChange={(e) => setForm((f) => ({ ...f, description: e.target.value }))}
              required
              maxLength={280}
              className="field w-full resize-y"
            />
          </div>
          <div className="flex flex-wrap gap-3">
            <Button type="submit" variant="ghost" disabled={busy}>
              {busy ? 'Saving…' : 'Add to agenda'}
            </Button>
            {!items.length && (
              <button
                type="button"
                onClick={loadDefaults}
                disabled={busy}
                className="font-body text-xs uppercase tracking-[0.15em] text-moss underline hover:text-fern disabled:opacity-50"
              >
                Load default times
              </button>
            )}
          </div>
        </form>

        {error && <p className="mt-4 font-body text-sm text-red-700">{error}</p>}

        <ol className="mt-8 space-y-4">
          {items.map((item) => (
            <li
              key={item.id}
              className="flex items-start justify-between gap-4 rounded-xl border border-sage/30 bg-ivory p-4"
            >
              <div>
                <p className="font-body text-xs uppercase tracking-[0.2em] text-moss">{item.time}</p>
                <p className="mt-1 font-display text-xl italic text-bark">{item.title}</p>
                <p className="mt-1 font-body text-sm text-bark/70">{item.description}</p>
              </div>
              <button
                type="button"
                onClick={() => handleDelete(item.id)}
                className="shrink-0 font-body text-xs uppercase tracking-[0.15em] text-red-700 hover:underline"
              >
                Remove
              </button>
            </li>
          ))}
          {!items.length && (
            <p className="font-body text-sm text-bark/50">
              No saved items yet — the invitation currently shows the default agenda until you add some here.
            </p>
          )}
        </ol>
      </div>
    </main>
  );
}
