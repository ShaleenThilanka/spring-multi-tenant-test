import { NavLink } from 'react-router-dom';

const links = [
  { to: '/admin/guests', label: 'Guests' },
  { to: '/admin/tables', label: 'Tables' },
  { to: '/admin/agenda', label: 'Agenda' },
  { to: '/admin/photos/preshoot', label: 'Photos' },
];

export default function AdminNav() {
  return (
    <nav className="mb-8 flex flex-wrap gap-x-5 gap-y-2 font-body text-xs uppercase tracking-[0.18em] text-bark/50">
      {links.map((link) => (
        <NavLink
          key={link.to}
          to={link.to}
          className={({ isActive }) =>
            `no-underline transition-colors hover:text-moss ${isActive ? 'text-moss' : ''}`
          }
        >
          {link.label}
        </NavLink>
      ))}
    </nav>
  );
}
