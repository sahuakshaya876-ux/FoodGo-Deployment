import { Link } from "react-router-dom";
import { Clock, Star } from "lucide-react";

export default function RestaurantCard({ restaurant }) {
  return (
    <Link to={`/restaurants/${restaurant.id}`} className="card group block overflow-hidden">
      <div className="relative h-40 w-full overflow-hidden bg-slate-100">
        <img
          src={restaurant.imageUrl || "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=600&q=60"}
          alt={restaurant.name}
          className="h-full w-full object-cover transition group-hover:scale-105"
        />
        {!restaurant.open && (
          <span className="absolute left-3 top-3 rounded-full bg-slate-900/80 px-3 py-1 text-xs font-semibold text-white">
            Closed
          </span>
        )}
        {restaurant.deliveryFee !== undefined && (
          <span className="absolute right-3 top-3 rounded-full bg-white/90 px-3 py-1 text-xs font-semibold text-slate-700">
            ₹{restaurant.deliveryFee} delivery
          </span>
        )}
      </div>
      <div className="space-y-1.5 p-4">
        <div className="flex items-start justify-between gap-2">
          <h3 className="font-semibold text-slate-900 line-clamp-1">{restaurant.name}</h3>
          <span className="flex shrink-0 items-center gap-1 rounded-md bg-green-50 px-1.5 py-0.5 text-xs font-bold text-green-700">
            <Star size={12} className="fill-green-700" />
            {Number(restaurant.averageRating || 0).toFixed(1)}
          </span>
        </div>
        <p className="text-sm text-slate-500 line-clamp-1">{restaurant.cuisineType || "Multi-cuisine"}</p>
        <div className="flex items-center gap-1 text-xs text-slate-400">
          <Clock size={13} />
          <span>{restaurant.estimatedDeliveryMinutes || 35} mins</span>
          <span className="mx-1">•</span>
          <span>{restaurant.city || "Nearby"}</span>
        </div>
      </div>
    </Link>
  );
}
