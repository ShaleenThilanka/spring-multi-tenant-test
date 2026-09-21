import axios from 'axios';
import { siteConfig } from '../config/siteConfig';

export const api = axios.create({
  baseURL: siteConfig.api.baseUrl,
});

function unwrap(response) {
  const payload = response?.data;
  if (payload && typeof payload === 'object' && 'data' in payload) {
    return payload.data;
  }
  return payload;
}

function asList(value) {
  if (Array.isArray(value)) return value;
  if (value == null) return [];
  return [value];
}

function normalizeGuest(guest) {
  if (!guest || typeof guest !== 'object') return guest;
  const id = guest.id ?? guest.guestId;
  return { ...guest, id, guestId: guest.guestId ?? id };
}

function normalizePhoto(photo) {
  if (!photo || typeof photo !== 'object') return photo;
  const url = photo.fileUrl ?? photo.filePath;
  return { ...photo, fileUrl: url, filePath: url };
}

export function apiMessage(err, fallback) {
  return err?.response?.data?.message || err?.message || fallback;
}

// --- Guest-facing ---

export const searchGuestByName = (name) =>
  api.get('/guests/search', { params: { name } }).then((r) => asList(unwrap(r)).map(normalizeGuest));

export const updateRsvp = (guestId, attending) =>
  api
    .put('/guests/rsvp', {
      guestId,
      rsvpStatus: attending ? 'ATTENDING' : 'NOT_ATTENDING',
    })
    .then((r) => unwrap(r));

export const uploadGuestPhoto = (guestId, file) => {
  const form = new FormData();
  form.append('file', file);
  return api.post('/photos/guest', form, { params: { guestId } }).then((r) => unwrap(r));
};

export const uploadGuestPhotos = (guestId, files) => {
  const form = new FormData();
  files.forEach((file) => form.append('files', file));
  return api.post('/photos/guest', form, { params: { guestId } }).then((r) => unwrap(r));
};

// --- Gallery ---

function parsePhotoPage(data, page, size) {
  if (data && Array.isArray(data.items)) {
    return {
      items: data.items.map(normalizePhoto),
      page: data.page ?? page,
      hasMore: Boolean(data.hasMore),
      total: data.total ?? data.items.length,
    };
  }
  const list = asList(data).map(normalizePhoto);
  const start = page * size;
  const items = list.slice(start, start + size);
  return {
    items,
    page,
    hasMore: start + size < list.length,
    total: list.length,
  };
}

export const getPreshootGallery = () =>
  api.get('/photos/preshoot').then((r) => asList(unwrap(r)).map(normalizePhoto));
export const getGuestUploadsGallery = () =>
  api.get('/photos/guest').then((r) => asList(unwrap(r)).map(normalizePhoto));

export const getPreshootGalleryPage = (page = 0, size = 12) =>
  api.get('/photos/preshoot', { params: { page, size } }).then((r) => parsePhotoPage(unwrap(r), page, size));
export const getGuestUploadsGalleryPage = (page = 0, size = 12) =>
  api.get('/photos/guest', { params: { page, size } }).then((r) => parsePhotoPage(unwrap(r), page, size));

// --- Admin: guests ---

export const adminListGuests = () => api.get('/guests').then((r) => asList(unwrap(r)).map(normalizeGuest));
export const adminCreateGuest = (guest) => api.post('/guests', guest).then((r) => unwrap(r));
export const adminAssignTable = (guestId, tableId) =>
  api.put('/guests/assign-table', { guestId, tableId }).then((r) => unwrap(r));

// --- Admin: tables ---

export const adminListTables = () => api.get('/wedding-table/visitor/all').then((r) => asList(unwrap(r)));
export const adminCreateTable = (table) =>
  api.post('/wedding-table/visitor/save', table).then((r) => unwrap(r));
export const adminGetTableGuests = (tableId) =>
  api.get(`/wedding-table/visitor/${tableId}`).then((r) => unwrap(r));

// --- Admin: stats ---

export const adminGetStats = () => api.get('/admin/stats').then((r) => unwrap(r));

// --- Admin: photos ---

export const adminUploadPreshootPhotos = (files) => {
  const form = new FormData();
  files.forEach((f) => form.append('files', f));
  return api.post('/photos/preshoot', form).then((r) => unwrap(r));
};

export const adminDeletePhoto = (photoId) => api.delete(`/photos/${photoId}`).then((r) => unwrap(r));

// --- Agenda ---

export const getAgenda = () => api.get('/agenda').then((r) => asList(unwrap(r)));
export const adminCreateAgendaItem = (item) => api.post('/agenda', item).then((r) => unwrap(r));
export const adminDeleteAgendaItem = (id) => api.delete(`/agenda/${id}`).then((r) => unwrap(r));
