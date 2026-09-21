export default function PageShell({ children, className = '' }) {
  return (
    <main className={`relative min-h-screen ${className}`}>
      {children}
    </main>
  );
}
