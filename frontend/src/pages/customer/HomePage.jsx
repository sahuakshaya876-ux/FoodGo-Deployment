import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { ArrowRight, Truck, ShieldCheck, Clock3 } from "lucide-react";
import { fetchRestaurants } from "../../api/restaurants";
import { fetchCategories } from "../../api/categories";
import RestaurantCard from "../../components/RestaurantCard";
import LoadingSpinner from "../../components/LoadingSpinner";

export default function HomePage() {
  const [restaurants, setRestaurants] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([fetchRestaurants(), fetchCategories()])
      .then(([restaurantsRes, categoriesRes]) => {
        setRestaurants(restaurantsRes.data || []);
        setCategories(categoriesRes.data || []);
      })
      .finally(() => setLoading(false));
  }, []);

  const topRated = [...restaurants].sort((a, b) => b.averageRating - a.averageRating).slice(0, 8);

  return (
    <div>
      <section className="bg-gradient-to-br from-brand-500 to-brand-700 text-white">
        <div className="mx-auto grid max-w-7xl items-center gap-8 px-4 py-16 md:grid-cols-2 md:py-24">
          <div>
            <h1 className="text-4xl font-black leading-tight md:text-5xl">
              Craving something delicious?
            </h1>
            <p className="mt-4 max-w-md text-brand-50">
              Order from your favorite local restaurants and get it delivered fast, fresh, and hot — right to your door.
            </p>
            <Link to="/restaurants" className="mt-8 inline-flex items-center gap-2 rounded-xl bg-white px-6 py-3 font-semibold text-brand-600 shadow-lg transition hover:bg-brand-50">
              Order Now <ArrowRight size={18} />
            </Link>
          </div>
          <img
            src="https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800&q=70"
            alt="Delicious food spread"
            className="hidden rounded-3xl shadow-2xl md:block"
          />
        </div>
      </section>

      <section className="mx-auto max-w-7xl px-4 py-10">
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
          {[
            { icon: Truck, title: "Fast Delivery", desc: "Hot food delivered in 30-40 minutes" },
            { icon: ShieldCheck, title: "Verified Restaurants", desc: "Quality checked & admin approved" },
            { icon: Clock3, title: "Live Order Tracking", desc: "Know exactly where your order is" },
          ].map((f) => (
            <div key={f.title} className="card flex items-center gap-4 p-5">
              <f.icon className="text-brand-500" size={32} />
              <div>
                <h3 className="font-semibold text-slate-900">{f.title}</h3>
                <p className="text-sm text-slate-500">{f.desc}</p>
              </div>
            </div>
          ))}
        </div>
      </section>

      {categories.length > 0 && (
        <section className="mx-auto max-w-7xl px-4 py-6">
          <h2 className="mb-4 text-2xl font-bold text-slate-900">Food Categories</h2>
          <div className="flex gap-4 overflow-x-auto pb-2">
            {categories.map((category) => (
              <Link
                to={`/restaurants?categoryId=${category.id}`}
                key={category.id}
                className="flex w-28 shrink-0 flex-col items-center gap-2 rounded-2xl bg-white p-3 text-center shadow-card transition hover:shadow-card-hover"
              >
                <img
                  src={category.imageUrl || "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=150&q=60"}
                  alt={category.name}
                  className="h-16 w-16 rounded-full object-cover"
                />
                <span className="text-xs font-medium text-slate-700 line-clamp-1">{category.name}</span>
              </Link>
            ))}
          </div>
        </section>
      )}

      <section className="mx-auto max-w-7xl px-4 py-10">
        <div className="mb-4 flex items-center justify-between">
          <h2 className="text-2xl font-bold text-slate-900">Popular Restaurants</h2>
          <Link to="/restaurants" className="text-sm font-semibold text-brand-500">
            View all
          </Link>
        </div>

        {loading ? (
          <LoadingSpinner label="Finding great restaurants near you..." />
        ) : topRated.length === 0 ? (
          <p className="text-slate-500">No restaurants available yet. Check back soon!</p>
        ) : (
          <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
            {topRated.map((restaurant) => (
              <RestaurantCard key={restaurant.id} restaurant={restaurant} />
            ))}
          </div>
        )}
      </section>
    </div>
  );
}
