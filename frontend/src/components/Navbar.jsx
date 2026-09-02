import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { ShoppingCart, User, Search, MapPin, Menu, X } from "lucide-react";
import { useAuth } from "../context/AuthContext";
import { useCart } from "../context/CartContext";

export default function Navbar() {
  const { user, logout } = useAuth();
  const { itemCount } = useCart();
  const navigate = useNavigate();
  const [query, setQuery] = useState("");
  const [mobileOpen, setMobileOpen] = useState(false);

  const handleSearch = (e) => {
    e.preventDefault();
    navigate(query ? `/restaurants?search=${encodeURIComponent(query)}` : "/restaurants");
    setMobileOpen(false);
  };

  const dashboardLink =
    user?.role === "ROLE_ADMIN"
      ? "/admin/dashboard"
      : user?.role === "ROLE_RESTAURANT_OWNER"
      ? "/restaurant/dashboard"
      : null;

  return (
    <header className="sticky top-0 z-40 border-b border-slate-100 bg-white/95 backdrop-blur">
      <div className="mx-auto flex max-w-7xl items-center gap-4 px-4 py-3">
        <Link to="/" className="flex shrink-0 items-center gap-2">
          <span className="flex h-9 w-9 items-center justify-center rounded-xl bg-brand-500 text-lg font-black text-white">
            F
          </span>
          <span className="text-xl font-extrabold tracking-tight text-slate-900">
            Food<span className="text-brand-500">Go</span>
          </span>
        </Link>

        <div className="hidden items-center gap-1 rounded-xl bg-slate-100 px-3 py-2 text-sm text-slate-500 lg:flex">
          <MapPin size={16} className="text-brand-500" />
          <span>Deliver to your location</span>
        </div>

        <form onSubmit={handleSearch} className="hidden flex-1 max-w-md items-center md:flex">
          <div className="flex w-full items-center gap-2 rounded-xl border border-slate-200 bg-slate-50 px-3 py-2">
            <Search size={18} className="text-slate-400" />
            <input
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder="Search restaurants or food..."
              className="w-full bg-transparent text-sm outline-none"
            />
          </div>
        </form>

        <nav className="ml-auto hidden items-center gap-5 text-sm font-medium text-slate-600 md:flex">
          <Link to="/restaurants" className="hover:text-brand-500">
            Restaurants
          </Link>
          {user?.role === "ROLE_CUSTOMER" && (
            <Link to="/wishlist" className="hover:text-brand-500">
              Wishlist
            </Link>
          )}
          {dashboardLink && (
            <Link to={dashboardLink} className="hover:text-brand-500">
              Dashboard
            </Link>
          )}

          {user?.role === "ROLE_CUSTOMER" && (
            <Link to="/cart" className="relative flex items-center hover:text-brand-500">
              <ShoppingCart size={22} />
              {itemCount > 0 && (
                <span className="absolute -right-2 -top-2 flex h-5 w-5 items-center justify-center rounded-full bg-brand-500 text-xs font-bold text-white">
                  {itemCount}
                </span>
              )}
            </Link>
          )}

          {user ? (
            <div className="group relative">
              <button className="flex items-center gap-2 rounded-full bg-slate-100 px-3 py-2">
                <User size={18} />
                <span>{user.fullName?.split(" ")[0]}</span>
              </button>
              <div className="invisible absolute right-0 mt-1 w-44 rounded-xl border border-slate-100 bg-white py-2 opacity-0 shadow-card transition group-hover:visible group-hover:opacity-100">
                <Link to="/profile" className="block px-4 py-2 text-sm hover:bg-slate-50">
                  My Profile
                </Link>
                {user.role === "ROLE_CUSTOMER" && (
                  <Link to="/orders" className="block px-4 py-2 text-sm hover:bg-slate-50">
                    My Orders
                  </Link>
                )}
                <button
                  onClick={logout}
                  className="block w-full px-4 py-2 text-left text-sm text-red-500 hover:bg-red-50"
                >
                  Logout
                </button>
              </div>
            </div>
          ) : (
            <Link to="/login" className="btn-primary !px-4 !py-2 text-sm">
              Login / Register
            </Link>
          )}
        </nav>

        <button className="ml-auto md:hidden" onClick={() => setMobileOpen((v) => !v)}>
          {mobileOpen ? <X /> : <Menu />}
        </button>
      </div>

      {mobileOpen && (
        <div className="border-t border-slate-100 px-4 py-3 md:hidden">
          <form onSubmit={handleSearch} className="mb-3 flex items-center gap-2 rounded-xl border border-slate-200 px-3 py-2">
            <Search size={18} className="text-slate-400" />
            <input
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder="Search..."
              className="w-full bg-transparent text-sm outline-none"
            />
          </form>
          <div className="flex flex-col gap-3 text-sm font-medium text-slate-600">
            <Link to="/restaurants" onClick={() => setMobileOpen(false)}>Restaurants</Link>
            {user?.role === "ROLE_CUSTOMER" && (
              <>
                <Link to="/cart" onClick={() => setMobileOpen(false)}>Cart ({itemCount})</Link>
                <Link to="/orders" onClick={() => setMobileOpen(false)}>My Orders</Link>
                <Link to="/wishlist" onClick={() => setMobileOpen(false)}>Wishlist</Link>
              </>
            )}
            {dashboardLink && <Link to={dashboardLink} onClick={() => setMobileOpen(false)}>Dashboard</Link>}
            {user ? (
              <>
                <Link to="/profile" onClick={() => setMobileOpen(false)}>My Profile</Link>
                <button onClick={logout} className="text-left text-red-500">Logout</button>
              </>
            ) : (
              <Link to="/login" onClick={() => setMobileOpen(false)}>Login / Register</Link>
            )}
          </div>
        </div>
      )}
    </header>
  );
}
