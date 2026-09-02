import { Outlet } from "react-router-dom";
import AdminSidebar from "./AdminSidebar";

export default function AdminLayout() {
  return (
    <div className="mx-auto flex max-w-7xl flex-col md:flex-row">
      <AdminSidebar />
      <div className="flex-1 p-4 md:p-8">
        <Outlet />
      </div>
    </div>
  );
}
