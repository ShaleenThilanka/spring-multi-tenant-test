export function downloadBlob(blob, filename) {
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  link.remove();
  URL.revokeObjectURL(url);
}

export function downloadCsv(filename, headers, rows) {
  const escape = (value) => `"${String(value ?? '').replace(/"/g, '""')}"`;
  const csv = [headers, ...rows].map((row) => row.map(escape).join(',')).join('\n');
  downloadBlob(new Blob([csv], { type: 'text/csv;charset=utf-8' }), filename);
}

export async function downloadImage(url, filename) {
  const response = await fetch(url);
  if (!response.ok) throw new Error('Could not download image');
  const blob = await response.blob();
  const ext = blob.type.includes('png') ? 'png' : blob.type.includes('webp') ? 'webp' : 'jpg';
  const name = filename.endsWith('.jpg') || filename.endsWith('.png') || filename.endsWith('.webp')
    ? filename
    : `${filename}.${ext}`;
  downloadBlob(blob, name);
}
