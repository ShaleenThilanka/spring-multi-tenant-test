# Shaleen & Kavindya — Wedding Website (Frontend)

React 18 + Vite frontend for the wedding site. Talks to a separate Spring Boot
backend over the REST API described in the project spec.

## Stack

- React 18 + Vite
- React Router v6
- Tailwind CSS (forest palette tokens in `tailwind.config.js`)
- Framer Motion (agenda modal)
- react-leaflet + OpenStreetMap tiles (venue map, no API key needed)
- axios (API client)

## Getting started

```bash
npm install
npm run dev       # http://localhost:5173
```

Set the backend URL via an env var (defaults to `http://localhost:8080/api`):

```bash
# .env.local
VITE_API_BASE_URL=https://your-domain.com/api
```

## Before launch — edit these

1. **`src/config/siteConfig.js`** — the single source of truth for names,
   dates, ceremony times, venue address, and the Google Maps link.
   - Replace the placeholder `venue.lat` / `venue.lng` with the exact pin for
     Ligness Green Odyssey.
   - Wedding date is currently set to **November 5, 2026** (confirmed).
2. **`public/audio/wedding-song.mp3`** — drop in a licensed mp3; see the
   README in that folder. The player starts paused (browsers block
   autoplay-with-sound).
3. Decide whether guest-uploaded gallery photos need admin approval before
   appearing publicly — the schema in the backend spec already supports a
   `Photo.approved` flag; the `/api/gallery/guest-uploads` endpoint should
   only return `approved = true` rows if you want that gate.

## Routes

Guest-facing (linked in nav):
- `/` — Landing (hero, agenda modal, map, thank-you)
- `/find-my-seat` — search by name, view table/RSVP, update RSVP
- `/gallery` — pre-shoot + guest-upload tabs, guest photo upload form

Admin (intentionally **not** linked anywhere in the UI — direct URL only,
no auth, matching the "no security hardening needed" spec):
- `/admin/guests`
- `/admin/tables`
- `/admin/photos/preshoot`

## Deploying (nginx + static build)

```bash
npm run build     # outputs to dist/
```

Serve `dist/` as static files via nginx, and reverse-proxy `/api` to the
Spring Boot backend (same pattern as other bizstarx projects — nginx +
systemd-managed JAR, no cloud services).

## Notes

- All colors/fonts are Tailwind tokens (`canopy`, `fern`, `moss`, `sage`,
  `ivory`, `gold`, `bark` / `font-display`, `font-body`) — keep new UI on
  these tokens rather than introducing new ad-hoc colors.
- Ambient leaves/fireflies and all animation respect
  `prefers-reduced-motion` (see `src/styles/index.css`).
- The API response shapes assumed by `src/api/client.js` (e.g. guest objects
  with `tableId`, `tableNumber`, `seatNumber`, `plusOneCount`) may need small
  adjustments once the real backend DTOs are finalized — the seat-availability
  math on `/admin/guests` and `/admin/tables` is computed client-side from the
  guest list as a safety net regardless of what the backend returns.
