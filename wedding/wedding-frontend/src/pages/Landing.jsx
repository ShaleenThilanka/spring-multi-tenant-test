import { useState } from 'react';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import PageShell from '../components/PageShell.jsx';
import AmbientAtmosphere from '../components/AmbientAtmosphere.jsx';
import AgendaModal from '../components/AgendaModal.jsx';
import VineDivider from '../components/VineDivider.jsx';
import LocationMap from '../components/LocationMap.jsx';
import Button from '../components/Button.jsx';
import CountdownTimer from '../components/CountdownTimer.jsx';
import ScrollCue from '../components/ScrollCue.jsx';
import Reveal from '../components/Reveal.jsx';
import FindSeatSection from '../components/FindSeatSection.jsx';
import PreshootPreview from '../components/PreshootPreview.jsx';
import MemoryUploadForm from '../components/MemoryUploadForm.jsx';
import NameFlourish from '../components/NameFlourish.jsx';
import { siteConfig } from '../config/siteConfig';
import { fadeUp, stagger } from '../lib/motion.js';

export default function Landing() {
  const [agendaOpen, setAgendaOpen] = useState(false);
  const { couple, wedding, venue, thankYouMessage } = siteConfig;

  return (
    <PageShell>
      {/* Hero */}
      <section className="relative flex min-h-[100svh] flex-col items-center justify-center overflow-hidden bg-ivory">
        <AmbientAtmosphere leafCount={0} fireflyCount={14} />
        <motion.div
          className="relative z-10 mx-auto flex w-full max-w-3xl flex-col items-center px-5 py-16 text-center text-bark sm:px-6"
          variants={stagger}
          initial="hidden"
          animate="show"
        >
          <motion.p
            variants={fadeUp}
            className="font-body text-[0.65rem] uppercase tracking-[0.35em] text-moss sm:text-xs"
          >
            Together with their families
          </motion.p>
          <motion.h1
            variants={fadeUp}
            className="mt-3 font-display text-5xl italic leading-[1.1] text-canopy sm:mt-4 sm:text-7xl"
          >
            <span className="block sm:inline">{couple.partnerOne}</span>
            <span className="block not-italic text-moss sm:inline sm:px-3">&amp;</span>
            <span className="block sm:inline">{couple.partnerTwo}</span>
          </motion.h1>
          <NameFlourish className="mt-2 h-8 w-52 sm:h-9 sm:w-64" />
          <motion.p variants={fadeUp} className="mt-2 font-display text-xl text-bark/80 sm:text-2xl">
            {wedding.displayDate}
          </motion.p>
          <motion.p variants={fadeUp} className="mt-1 font-body text-xs text-bark/60 sm:text-sm">
            {venue.name}, {venue.addressLine}
          </motion.p>

          <motion.div variants={fadeUp} className="mt-8 sm:mt-10">
            <CountdownTimer />
          </motion.div>

          <motion.div variants={fadeUp}>
            <Button className="mt-8 sm:mt-10" onClick={() => setAgendaOpen(true)}>
              View Agenda
            </Button>
          </motion.div>
        </motion.div>
        <div className="absolute inset-x-0 bottom-6 z-10 flex justify-center">
          <ScrollCue targetId="find-seat" />
        </div>
      </section>

      <VineDivider className="bg-ivory" />

      {/* Find your seat + RSVP */}
      <section id="find-seat" className="bg-ivory px-6 py-20 sm:px-10">
        <Reveal>
          <FindSeatSection />
        </Reveal>
      </section>

      <VineDivider className="bg-ivory" />

      {/* Gallery preview */}
      <section className="bg-ivory px-6 py-20 sm:px-10">
        <Reveal className="text-center">
          <p className="font-body text-xs uppercase tracking-[0.3em] text-moss">A preview</p>
          <h2 className="mt-2 font-display text-4xl italic text-bark">Our Pre-Shoot</h2>
        </Reveal>
        <div className="mt-10">
          <PreshootPreview />
        </div>
        <div className="mt-8 text-center">
          <Link
            to="/gallery"
            className="font-body text-xs uppercase tracking-[0.2em] text-moss underline hover:text-fern"
          >
            View the full gallery
          </Link>
        </div>
      </section>

      <VineDivider className="bg-ivory" />

      {/* Upload your memory */}
      <section className="bg-ivory px-6 py-20 sm:px-10">
        <Reveal className="text-center">
          <p className="font-body text-xs uppercase tracking-[0.3em] text-moss">Share the day</p>
          <h2 className="mt-2 font-display text-4xl italic text-canopy">Upload Your Best Memory</h2>
          <p className="mx-auto mt-3 max-w-md font-body text-sm text-bark/70">
            Capture something on the wedding day itself? We'd love to see it.
          </p>
        </Reveal>
        <div className="mt-10">
          <MemoryUploadForm
            title="Upload your best memory"
            subtitle="Share the most memorable photo you took on our wedding day."
          />
        </div>
      </section>

      <VineDivider className="bg-ivory" />

      {/* Venue location */}
      <section className="bg-ivory px-6 py-20 sm:px-10">
        <Reveal className="mx-auto max-w-4xl text-center">
          <p className="font-body text-xs uppercase tracking-[0.3em] text-moss">Getting there</p>
          <h2 className="mt-2 font-display text-4xl italic text-bark">{venue.name}</h2>
          <p className="mt-1 font-body text-bark/70">{venue.addressLine}</p>
          <div className="mt-8 text-left">
            <LocationMap {...venue} />
          </div>
          <a
            href={venue.googleMapsUrl}
            target="_blank"
            rel="noreferrer"
            className="mt-4 inline-block font-body text-sm uppercase tracking-[0.15em] text-moss underline hover:text-fern"
          >
            Open directions in Google Maps
          </a>
        </Reveal>
      </section>

      <VineDivider className="bg-ivory" />

      {/* Thank you */}
      <section className="bg-ivory px-6 py-24 text-center text-bark sm:px-10">
        <Reveal className="mx-auto max-w-xl">
          <p className="font-body text-xs uppercase tracking-[0.3em] text-moss">With gratitude</p>
          <p className="mt-6 font-display text-3xl italic leading-relaxed text-canopy">{thankYouMessage}</p>
          <p className="mt-8 font-display text-xl text-moss">
            {couple.partnerOne} &amp; {couple.partnerTwo}
          </p>
        </Reveal>
      </section>

      <AgendaModal open={agendaOpen} onClose={() => setAgendaOpen(false)} />
    </PageShell>
  );
}
