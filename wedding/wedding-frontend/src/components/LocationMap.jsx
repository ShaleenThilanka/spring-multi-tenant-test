import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import L from 'leaflet';

const rosePin = L.divIcon({
  className: 'venue-map-pin',
  html: `<svg viewBox="0 0 48 72" width="22" height="32" aria-hidden="true">
    <path d="M24 38 C 24 48, 24 58, 24 68" stroke="#4A6F50" stroke-width="1.6" fill="none"/>
    <g transform="translate(24 22)">
      <path d="M0 -11 C -16 -14, -20 2, -9 14 C -16 2, -11 -8, 0 -11 Z" fill="#8E1A2C"/>
      <path d="M0 -11 C 16 -14, 20 2, 9 14 C 16 2, 11 -8, 0 -11 Z" fill="#C41E3A"/>
      <path d="M-10 -1 C -21 4, -19 17, -3 15 C -13 10, -15 3, -10 -1 Z" fill="#C41E3A"/>
      <path d="M10 -1 C 21 4, 19 17, 3 15 C 13 10, 15 3, 10 -1 Z" fill="#DC2F45"/>
      <path d="M-1 1 C -4 -5, 8 -7, 5 2 C 2 -2, 0 -1, -1 1 Z" fill="#F4A0A8"/>
    </g>
  </svg>`,
  iconSize: [22, 32],
  iconAnchor: [11, 30],
  popupAnchor: [0, -28],
});

export default function LocationMap({ name, addressLine, lat, lng, googleMapsUrl, zoom = 15, height = 360 }) {
  return (
    <div className="venue-map overflow-hidden rounded-2xl border border-sage/40 shadow-lg">
      <MapContainer
        center={[lat, lng]}
        zoom={zoom}
        scrollWheelZoom={false}
        style={{ height, width: '100%' }}
      >
        <TileLayer
          attribution='&copy; OpenStreetMap contributors'
          url="https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png"
        />
        <Marker position={[lat, lng]} icon={rosePin}>
          <Popup>
            <strong>{name}</strong>
            <br />
            {addressLine}
            <br />
            <a href={googleMapsUrl} target="_blank" rel="noreferrer">
              Open in Google Maps
            </a>
          </Popup>
        </Marker>
      </MapContainer>
    </div>
  );
}
