import { NavLink } from "react-router-dom";
import { LayoutDashboard, Store, UtensilsCrossed, ClipboardList, BarChart3 } from "lucide-react";

const links = [
  { to: "/restaurant/dashboard", label: "Dashboard", icon: LayoutDashboard },
  { to: "/restaurant/profile", label: "Restaurant Profile", icon: Store },
  { to: "/restaurant/menu", label: "Menu Management", icon: UtensilsCrossed },
  { to: "/restaurant/orders", label: "Orders", icon: ClipboardList },
  { to: "/restaurant/analytics", label: "Analytics", icon: BarChart3 },
];

export default function RestaurantSidebar() {
  return (
    <aside className="w-full shrink-0 border-r border-slate-100 bg-white p-4 md:w-60">
      <nav className="flex gap-2 overflow-x-auto md:flex-col md:overflow-visible">
        {links.map((link) => (
          <NavLink
            key={link.to}
            to={link.to}
            className={({ isActive }) =>
              `flex shrink-0 items-center gap-3 rounded-xl px-4 py-2.5 text-sm font-medium transition ${
                isActive ? "bg-brand-50 text-brand-600" : "text-slate-600 hover:bg-slate-50"
              }`
            }
          >
            <link.icon size={18} />
            {link.label}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}
