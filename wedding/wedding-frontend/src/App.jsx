import { Routes, Route, useLocation } from 'react-router-dom';
import { AnimatePresence, motion } from 'framer-motion';
import Landing from './pages/Landing.jsx';
import FindMySeat from './pages/FindMySeat.jsx';
import Gallery from './pages/Gallery.jsx';
import AdminGuests from './pages/admin/AdminGuests.jsx';
import AdminTables from './pages/admin/AdminTables.jsx';
import AdminAgenda from './pages/admin/AdminAgenda.jsx';
import AdminPreshootPhotos from './pages/admin/AdminPreshootPhotos.jsx';
import MusicPlayer from './components/MusicPlayer.jsx';
import EnvelopeSplash from './components/EnvelopeSplash.jsx';
import AmbientAtmosphere from './components/AmbientAtmosphere.jsx';
import { elegantEase } from './lib/motion.js';

function AnimatedRoutes() {
  const location = useLocation();
  const isAdmin = location.pathname.startsWith('/admin');

  return (
    <AnimatePresence mode="wait">
      <motion.div
        key={location.pathname}
        initial={isAdmin ? false : { opacity: 0, y: 22 }}
        animate={{ opacity: 1, y: 0 }}
        exit={isAdmin ? undefined : { opacity: 0, y: -14 }}
        transition={{ duration: 0.7, ease: elegantEase }}
      >
        <Routes location={location}>
          <Route path="/" element={<Landing />} />
          <Route path="/find-my-seat" element={<FindMySeat />} />
          <Route path="/gallery" element={<Gallery />} />

          {/* Admin routes: intentionally not linked from any nav */}
          <Route path="/admin/guests" element={<AdminGuests />} />
          <Route path="/admin/tables" element={<AdminTables />} />
          <Route path="/admin/agenda" element={<AdminAgenda />} />
          <Route path="/admin/photos/preshoot" element={<AdminPreshootPhotos />} />
        </Routes>
      </motion.div>
    </AnimatePresence>
  );
}

export default function App() {
  const { pathname } = useLocation();
  const isAdmin = pathname.startsWith('/admin');

  return (
    <div className="min-h-screen bg-ivory">
      {!isAdmin && <AmbientAtmosphere leafCount={10} fireflyCount={0} fixed />}
      <EnvelopeSplash />
      {!isAdmin && <MusicPlayer />}
      <AnimatedRoutes />
    </div>
  );
}
