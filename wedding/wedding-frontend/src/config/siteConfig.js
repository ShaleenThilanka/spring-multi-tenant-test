// Centralized, editable site details.
// Change everything here — no other component should hardcode these values.

export const siteConfig = {
  couple: {
    partnerOne: 'Shaleen',
    partnerTwo: 'Kavindya',
  },

  // Wedding date confirmed as November 5, 2026 (Ligness Green Odyssey booking).
  wedding: {
    date: '2026-11-05',
    displayDate: 'November 5, 2026',
    engagementTime: '9:30 AM',
    poruwaTime: '11:00 AM',
    receptionTime: '6:30 PM',
  },

  venue: {
    name: 'Ligness Green Odyssey',
    addressLine: 'Waduraba, Galle, Sri Lanka',
    // TODO: replace with the exact Google Maps pin for the venue before launch.
    lat: 6.0535,
    lng: 80.2210,
    googleMapsUrl: 'https://maps.google.com/?q=Ligness+Green+Odyssey+Waduraba+Galle',
    zoom: 15,
  },

  // TODO: replace with the actual guest hotel/accommodation details.
  hotel: {
    name: 'Jetwing Lighthouse',
    addressLine: 'Dadella, Galle, Sri Lanka',
    lat: 6.0392,
    lng: 80.2113,
    googleMapsUrl: 'https://maps.google.com/?q=Jetwing+Lighthouse+Galle',
    zoom: 14,
    note: 'A block of rooms is reserved for out-of-town guests — mention the wedding when booking.',
  },

  // ISO datetime (with offset) the countdown counts down to — the start of the engagement ceremony.
  countdownTarget: '2026-11-05T09:30:00+05:30',

  agenda: [
    { time: '9:30 AM', title: 'Engagement Ceremony', description: 'Family and close friends gather for the morning engagement rites.' },
    { time: '11:00 AM', title: 'Poruwa Ceremony', description: 'Traditional Poruwa ceremony beneath the canopy.' },
    { time: '1:00 PM', title: 'Lunch', description: 'A shared meal for all guests in attendance.' },
    { time: '6:30 PM', title: 'Reception', description: 'Evening reception, dinner, and dancing under the fireflies.' },
  ],

  music: {
    // Drop a licensed mp3 at this path — see /public/audio/README.md.
    src: '/audio/wedding-song.mp3',
    title: 'Our Song',
  },

  splash: {
    image: '/images/splash.png',
    sealedMs: 3000,
  },

  api: {
    // Spring Boot wedding-service (server.port=7001, paths under /api/v1).
    baseUrl: import.meta.env.VITE_API_BASE_URL || 'http://localhost:7001/api/v1',
  },

  thankYouMessage:
    "To everyone who has shaped our story so far — thank you for walking this next part of it with us.",
};

export default siteConfig;
