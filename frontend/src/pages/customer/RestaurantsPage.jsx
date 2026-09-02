import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { SlidersHorizontal } from "lucide-react";
import { fetchRestaurants } from "../../api/restaurants";
import RestaurantCard from "../../components/RestaurantCard";
import LoadingSpinner from "../../components/LoadingSpinner";
import EmptyState from "../../components/EmptyState";

export default function RestaurantsPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [sortBy, setSortBy] = useState("rating");

  const search = searchParams.get("search") || "";
  const cuisineType = searchParams.get("cuisineType") || "";

  useEffect(() => {
    setLoading(true);
    fetchRestaurants({ search: search || undefined, cuisineType: cuisineType || undefined })
      .then((res) => setRestaurants(res.data || []))
      .finally(() => setLoading(false));
  }, [search, cuisineType]);

  const sorted = [...restaurants].sort((a, b) => {
    if (sortBy === "rating") return b.averageRating - a.averageRating;
    if (sortBy === "delivery") return a.estimatedDeliveryMinutes - b.estimatedDeliveryMinutes;
    if (sortBy === "fee") return a.deliveryFee - b.deliveryFee;
    return 0;
  });

  const cuisines = ["North Indian", "South Indian", "Chinese", "Italian", "Fast Food", "Desserts"];

  return (
    <div className="mx-auto max-w-7xl px-4 py-8">
      <h1 className="mb-1 text-2xl font-bold text-slate-900">
        {search ? `Results for "${search}"` : "All Restaurants"}
      </h1>
      <p className="mb-6 text-sm text-slate-500">{sorted.length} restaurants found</p>

      <div className="mb-6 flex flex-wrap items-center gap-3">
        <button
          onClick={() => setSearchParams(search ? { search } : {})}
          className={`rounded-full px-4 py-1.5 text-sm font-medium ${
            !cuisineType ? "bg-brand-500 text-white" : "bg-white text-slate-600 shadow-card"
          }`}
        >
          All
        </button>
        {cuisines.map((c) => (
          <button
            key={c}
            onClick={() => setSearchParams({ ...(search ? { search } : {}), cuisineType: c })}
            className={`rounded-full px-4 py-1.5 text-sm font-medium ${
              cuisineType === c ? "bg-brand-500 text-white" : "bg-white text-slate-600 shadow-card"
            }`}
          >
            {c}
          </button>
        ))}

        <div className="ml-auto flex items-center gap-2 text-sm">
          <SlidersHorizontal size={16} className="text-slate-400" />
          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
            className="rounded-lg border border-slate-200 bg-white px-2 py-1.5"
          >
            <option value="rating">Sort by rating</option>
            <option value="delivery">Sort by delivery time</option>
            <option value="fee">Sort by delivery fee</option>
          </select>
        </div>
      </div>

      {loading ? (
        <LoadingSpinner />
      ) : sorted.length === 0 ? (
        <EmptyState title="No restaurants found" description="Try a different search or check back later." />
      ) : (
        <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
          {sorted.map((restaurant) => (
            <RestaurantCard key={restaurant.id} restaurant={restaurant} />
          ))}
        </div>
      )}
    </div>
  );
}
