import { Outlet } from "react-router-dom";
import RestaurantSidebar from "./RestaurantSidebar";

export default function RestaurantOwnerLayout() {
  return (
    <div className="mx-auto flex max-w-7xl flex-col md:flex-row">
      <RestaurantSidebar />
      <div className="flex-1 p-4 md:p-8">
        <Outlet />
      </div>
    </div>
  );
}
