import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { fetchAllRestaurantsAdmin, updateRestaurantStatusAdmin } from "../../api/admin";
import LoadingSpinner from "../../components/LoadingSpinner";

const STATUS_OPTIONS = ["PENDING_APPROVAL", "APPROVED", "REJECTED", "DISABLED"];

export default function AdminRestaurantsPage() {
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState("ALL");

  const load = () => {
    fetchAllRestaurantsAdmin()
      .then((res) => setRestaurants(res.data || []))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
  }, []);

  const handleStatusChange = async (id, status) => {
    try {
      await updateRestaurantStatusAdmin(id, status);
      toast.success("Restaurant status updated");
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not update restaurant");
    }
  };

  const filtered = filter === "ALL" ? restaurants : restaurants.filter((r) => r.status === filter);

  if (loading) return <LoadingSpinner />;

  return (
    <div>
      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-900">Manage Restaurants</h1>
        <select value={filter} onChange={(e) => setFilter(e.target.value)} className="rounded-lg border border-slate-200 px-3 py-2 text-sm">
          <option value="ALL">All statuses</option>
          {STATUS_OPTIONS.map((s) => (
            <option key={s} value={s}>{s.replaceAll("_", " ")}</option>
          ))}
        </select>
      </div>

      {filtered.length === 0 ? (
        <p className="text-slate-500">No restaurants found.</p>
      ) : (
        <div className="space-y-4">
          {filtered.map((restaurant) => (
            <div key={restaurant.id} className="card flex flex-wrap items-center justify-between gap-3 p-4">
              <div>
                <h3 className="font-semibold text-slate-900">{restaurant.name}</h3>
                <p className="text-sm text-slate-500">{restaurant.cuisineType} • {restaurant.city}</p>
                <span className="mt-1 inline-block rounded-full bg-slate-100 px-2.5 py-1 text-xs font-semibold text-slate-600">
                  {restaurant.status.replaceAll("_", " ")}
                </span>
              </div>
              <div className="flex gap-2">
                {restaurant.status === "PENDING_APPROVAL" && (
                  <>
                    <button onClick={() => handleStatusChange(restaurant.id, "APPROVED")} className="btn-primary !px-3 !py-1.5 text-sm">
                      Approve
                    </button>
                    <button onClick={() => handleStatusChange(restaurant.id, "REJECTED")} className="btn-secondary !text-red-500 !px-3 !py-1.5 text-sm">
                      Reject
                    </button>
                  </>
                )}
                {restaurant.status === "APPROVED" && (
                  <button onClick={() => handleStatusChange(restaurant.id, "DISABLED")} className="btn-secondary !text-red-500 !px-3 !py-1.5 text-sm">
                    Disable
                  </button>
                )}
                {(restaurant.status === "DISABLED" || restaurant.status === "REJECTED") && (
                  <button onClick={() => handleStatusChange(restaurant.id, "APPROVED")} className="btn-primary !px-3 !py-1.5 text-sm">
                    Approve
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
