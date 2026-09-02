import { Outlet } from "react-router-dom";
import { Toaster } from "react-hot-toast";
import Navbar from "./Navbar";
import Footer from "./Footer";

export default function Layout() {
  return (
    <div className="flex min-h-screen flex-col">
      <Toaster position="top-center" />
      <Navbar />
      <main className="flex-1">
        <Outlet />
      </main>
      <Footer />
    </div>
  );
}
