import { Link } from "react-router-dom";

export default function Footer() {
  return (
    <footer className="mt-16 border-t border-slate-100 bg-white">
      <div className="mx-auto grid max-w-7xl grid-cols-2 gap-8 px-4 py-10 text-sm text-slate-500 md:grid-cols-4">
        <div>
          <div className="mb-3 flex items-center gap-2">
            <span className="flex h-8 w-8 items-center justify-center rounded-lg bg-brand-500 font-black text-white">
              F
            </span>
            <span className="text-lg font-extrabold text-slate-900">FoodGo</span>
          </div>
          <p>Delicious food, delivered fast to your doorstep.</p>
        </div>
        <div>
          <h4 className="mb-3 font-semibold text-slate-800">Company</h4>
          <ul className="space-y-2">
            <li><Link to="/">About Us</Link></li>
            <li><Link to="/">Careers</Link></li>
            <li><Link to="/">Blog</Link></li>
          </ul>
        </div>
        <div>
          <h4 className="mb-3 font-semibold text-slate-800">For Partners</h4>
          <ul className="space-y-2">
            <li><Link to="/register">Partner with us</Link></li>
            <li><Link to="/restaurant/dashboard">Restaurant Dashboard</Link></li>
          </ul>
        </div>
        <div>
          <h4 className="mb-3 font-semibold text-slate-800">Support</h4>
          <ul className="space-y-2">
            <li><Link to="/">Help Center</Link></li>
            <li><Link to="/">Terms of Service</Link></li>
            <li><Link to="/">Privacy Policy</Link></li>
          </ul>
        </div>
      </div>
      <div className="border-t border-slate-100 py-4 text-center text-xs text-slate-400">
        © {new Date().getFullYear()} FoodGo. All rights reserved.
      </div>
    </footer>
  );
}
