import { useEffect, useRef, useState } from 'react';
import { siteConfig } from '../config/siteConfig';

export default function MusicPlayer() {
  const audioRef = useRef(null);
  const userPausedRef = useRef(false);
  const [playing, setPlaying] = useState(false);

  const play = async () => {
    const audio = audioRef.current;
    if (!audio) return false;
    try {
      await audio.play();
      userPausedRef.current = false;
      setPlaying(true);
      return true;
    } catch {
      setPlaying(false);
      return false;
    }
  };

  const pause = () => {
    const audio = audioRef.current;
    if (!audio) return;
    userPausedRef.current = true;
    audio.pause();
    setPlaying(false);
  };

  const toggle = () => {
    if (playing) pause();
    else play();
  };

  useEffect(() => {
    const audio = audioRef.current;
    if (!audio) return;

    const syncPlaying = () => setPlaying(!audio.paused);

    const tryPlay = () => {
      if (userPausedRef.current) return;
      play();
    };

    const unlock = () => {
      if (userPausedRef.current || !audio.paused) return;
      tryPlay();
    };

    audio.addEventListener('play', syncPlaying);
    audio.addEventListener('pause', syncPlaying);
    tryPlay();

    const events = ['pointerdown', 'touchstart', 'keydown'];
    events.forEach((event) => document.addEventListener(event, unlock));

    return () => {
      audio.pause();
      audio.removeEventListener('play', syncPlaying);
      audio.removeEventListener('pause', syncPlaying);
      events.forEach((event) => document.removeEventListener(event, unlock));
    };
  }, []);

  return (
    <div className="fixed bottom-5 right-5 z-50">
      <audio
        ref={audioRef}
        src={siteConfig.music.src}
        loop
        preload="auto"
        autoPlay
        playsInline
      />
      <button
        type="button"
        onClick={toggle}
        aria-label={playing ? `Pause ${siteConfig.music.title}` : `Play ${siteConfig.music.title}`}
        aria-pressed={playing}
        className="flex h-12 w-12 items-center justify-center rounded-full bg-fern text-ivory shadow-lg shadow-canopy/40 transition-transform hover:scale-105 focus-visible:scale-105"
      >
        {playing ? (
          <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
            <rect x="2" y="1" width="4" height="14" rx="1" />
            <rect x="10" y="1" width="4" height="14" rx="1" />
          </svg>
        ) : (
          <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
            <path d="M3 1.5 L14 8 L3 14.5 Z" />
          </svg>
        )}
      </button>
    </div>
  );
}
