import { useEffect, useState } from 'react';
import { motion } from 'framer-motion';
import Button from './Button.jsx';
import { apiMessage, searchGuestByName, uploadGuestPhotos } from '../api/client.js';

export default function MemoryUploadForm({
  title = 'Upload your best memory',
  subtitle = "Took a great shot on the wedding day? Share it and we'll add it to Our Moments.",
  theme = 'light', // 'light' | 'dark'
}) {
  const [step, setStep] = useState('name'); // name | guest | done
  const [name, setName] = useState('');
  const [guest, setGuest] = useState(null);
  const [files, setFiles] = useState([]);
  const [previews, setPreviews] = useState([]);
  const [status, setStatus] = useState('idle'); // idle | loading
  const [error, setError] = useState('');
  const [uploadedCount, setUploadedCount] = useState(0);

  const isDark = theme === 'dark';

  useEffect(() => {
    return () => {
      previews.forEach((url) => URL.revokeObjectURL(url));
    };
  }, [previews]);

  const findGuest = async (e) => {
    e.preventDefault();
    if (!name.trim()) return;
    setStatus('loading');
    setError('');
    try {
      const result = await searchGuestByName(name.trim());
      const found = Array.isArray(result) ? result[0] : result;
      if (found?.id) {
        setGuest(found);
        setStep('guest');
      } else {
        setError("We couldn't find that name. Try it exactly as it appears on your invitation.");
      }
    } catch (err) {
      setError(apiMessage(err, "We couldn't find that name. Please try again."));
    } finally {
      setStatus('idle');
    }
  };

  const addFiles = (incoming) => {
    if (!incoming?.length) return;
    const next = [...files, ...Array.from(incoming)];
    setFiles(next);
    setPreviews((current) => {
      current.forEach((url) => URL.revokeObjectURL(url));
      return next.map((file) => URL.createObjectURL(file));
    });
    setError('');
  };

  const removeFile = (index) => {
    const next = files.filter((_, i) => i !== index);
    setFiles(next);
    setPreviews((current) => {
      current.forEach((url) => URL.revokeObjectURL(url));
      return next.map((file) => URL.createObjectURL(file));
    });
  };

  const submitPhotos = async (e) => {
    e.preventDefault();
    if (!files.length || !guest?.id) return;
    setStatus('loading');
    setError('');
    try {
      await uploadGuestPhotos(guest.id, files);
      setUploadedCount(files.length);
      setStep('done');
    } catch (err) {
      setError(apiMessage(err, 'The photos could not be uploaded. Please try again.'));
    } finally {
      setStatus('idle');
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      whileInView={{ opacity: 1, y: 0 }}
      viewport={{ once: true, amount: 0.3 }}
      transition={{ duration: 0.6, ease: 'easeOut' }}
      className={`mx-auto max-w-md rounded-2xl border p-6 ${
        isDark ? 'border-sage/30 bg-fern/60 text-ivory' : 'border-moss/20 bg-ivory text-bark'
      }`}
    >
      <h3 className="font-display text-2xl italic">{title}</h3>
      <p className={`mt-1 font-body text-sm ${isDark ? 'text-ivory/70' : 'text-bark/70'}`}>{subtitle}</p>

      {step === 'name' && (
        <form onSubmit={findGuest} className="mt-5 flex flex-col gap-3 sm:flex-row">
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Your name"
            aria-label="Your name"
            className={`flex-1 rounded-full border px-5 py-3 font-body focus:border-gold focus:outline-none ${
              isDark ? 'border-sage/40 bg-canopy/40 text-ivory placeholder:text-ivory/40' : 'border-moss/25 bg-ivory text-bark placeholder:text-bark/40'
            }`}
          />
          <Button type="submit" variant="ghost" disabled={status === 'loading'}>
            Continue
          </Button>
        </form>
      )}

      {step === 'guest' && (
        <form onSubmit={submitPhotos} className="mt-5 flex flex-col gap-3">
          <p className={`font-body text-sm ${isDark ? 'text-ivory/70' : 'text-bark/70'}`}>
            Uploading as <strong>{guest.name}</strong>.{' '}
            <button type="button" className="underline" onClick={() => setStep('name')}>
              Not you?
            </button>
          </p>

          <label
            htmlFor="memory-file"
            className={`flex cursor-pointer flex-col items-center justify-center gap-2 rounded-xl border-2 border-dashed p-6 text-center transition-colors ${
              isDark ? 'border-sage/40 hover:border-gold' : 'border-moss/30 hover:border-moss'
            }`}
          >
            <span className="font-body text-sm uppercase tracking-[0.15em]">Tap to choose photos</span>
            <span className={`font-body text-xs ${isDark ? 'text-ivory/50' : 'text-bark/50'}`}>
              JPG or PNG — you can select more than one
            </span>
            <input
              id="memory-file"
              type="file"
              accept="image/*"
              multiple
              onChange={(e) => {
                addFiles(e.target.files);
                e.target.value = '';
              }}
              className="hidden"
            />
          </label>

          {previews.length > 0 && (
            <ul className="grid grid-cols-3 gap-2">
              {previews.map((src, index) => (
                <li key={src} className="relative">
                  <img src={src} alt="" className="aspect-square w-full rounded-lg object-cover" />
                  <button
                    type="button"
                    onClick={() => removeFile(index)}
                    className="absolute right-1 top-1 rounded-full bg-canopy/80 px-1.5 font-body text-[0.65rem] uppercase text-ivory"
                    aria-label="Remove photo"
                  >
                    ×
                  </button>
                </li>
              ))}
            </ul>
          )}

          <Button type="submit" variant="ghost" disabled={!files.length || status === 'loading'}>
            {status === 'loading'
              ? 'Uploading…'
              : files.length > 1
                ? `Upload ${files.length} photos`
                : 'Upload photo'}
          </Button>
        </form>
      )}

      {step === 'done' && (
        <p className="mt-5 font-body text-moss">
          Thank you — {uploadedCount > 1 ? `${uploadedCount} photos were` : 'your photo was'} uploaded
          and will appear in the gallery shortly.
        </p>
      )}

      {error && (
        <p className={`mt-3 font-body text-sm ${isDark ? 'text-red-300' : 'text-red-700'}`}>{error}</p>
      )}
    </motion.div>
  );
}
