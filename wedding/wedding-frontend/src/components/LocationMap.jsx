import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import L from 'leaflet';

const rosePin = L.divIcon({
  className: 'venue-map-pin',
  html: '<span class="venue-map-pin__dot"></span>',
  iconSize: [28, 36],
  iconAnchor: [14, 34],
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
