import { useEffect, useMemo, useState } from 'react';
import Button from '../../components/Button.jsx';
import AdminNav from '../../components/AdminNav.jsx';
import { adminListTables, adminListGuests, adminCreateTable, apiMessage } from '../../api/client.js';

export default function AdminTables() {
  const [tables, setTables] = useState([]);
  const [guests, setGuests] = useState([]);
  const [form, setForm] = useState({ tableNumber: '', capacity: 12 });
  const [error, setError] = useState('');

  const load = async () => {
    const [t, g] = await Promise.all([adminListTables(), adminListGuests()]);
    setTables(t);
    setGuests(g);
  };

  useEffect(() => {
    load().catch((err) => {
      setError(apiMessage(err, 'Could not load tables. Start the wedding service on port 7001.'));
    });
  }, []);

  const occupancy = useMemo(() => {
    const map = {};
    for (const t of tables) map[t.id] = 0;
    for (const g of guests) {
      if (g.tableId != null) {
        map[g.tableId] = (map[g.tableId] ?? 0) + 1 + (g.plusOneCount || 0);
      }
    }
    return map;
  }, [guests, tables]);

  const handleCreate = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await adminCreateTable({
        tableNumber: Number(form.tableNumber),
        capacity: Number(form.capacity) || 12,
      });
      setForm({ tableNumber: '', capacity: 12 });
      await load();
    } catch (err) {
      setError(apiMessage(err, 'Could not create table — number may already exist.'));
    }
  };

  return (
    <main className="min-h-screen bg-ivory px-6 py-12 sm:px-10" style={{ colorScheme: 'light' }}>
      <div className="mx-auto max-w-3xl">
        <AdminNav />
        <p className="font-body text-xs uppercase tracking-[0.3em] text-moss">Admin</p>
        <h1 className="mt-1 font-display text-3xl italic text-bark">Tables</h1>

        <form onSubmit={handleCreate} className="mt-6 flex flex-wrap items-end gap-3 rounded-xl border border-sage/30 bg-sage/10 p-4">
          <div>
            <label htmlFor="table-number" className="block font-body text-xs uppercase tracking-wide text-bark/60">Table number</label>
            <input
              id="table-number"
              type="number"
              min="1"
              step="1"
              inputMode="numeric"
              value={form.tableNumber}
              onChange={(e) => setForm((f) => ({ ...f, tableNumber: e.target.value }))}
              required
              className="field w-28"
            />
          </div>
          <div>
            <label htmlFor="table-capacity" className="block font-body text-xs uppercase tracking-wide text-bark/60">Capacity</label>
            <input
              id="table-capacity"
              type="number"
              min="1"
              step="1"
              inputMode="numeric"
              value={form.capacity}
              onChange={(e) => setForm((f) => ({ ...f, capacity: e.target.value }))}
              className="field w-24"
            />
          </div>
          <Button type="submit" variant="ghost">Create table</Button>
        </form>

        {error && <p className="mt-4 font-body text-sm text-red-700">{error}</p>}

        <div className="mt-8 grid grid-cols-2 gap-4 sm:grid-cols-3">
          {tables.map((table) => {
            const occupied = occupancy[table.id] ?? 0;
            const full = occupied >= table.capacity;
            return (
              <div
                key={table.id}
                className={`rounded-xl border p-4 font-body ${
                  full ? 'border-moss bg-moss/10' : 'border-sage/30 bg-fern/5'
                }`}
              >
                <p className="font-display text-xl italic text-bark">Table {table.tableNumber}</p>
                <p className="mt-1 text-sm text-bark/70">
                  {occupied} / {table.capacity} seated
                </p>
                {full && <p className="mt-1 text-xs uppercase tracking-wide text-moss">Full</p>}
              </div>
            );
          })}
          {!tables.length && <p className="col-span-full text-bark/50">No tables yet.</p>}
        </div>
      </div>
    </main>
  );
}
