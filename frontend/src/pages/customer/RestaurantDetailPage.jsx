import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Clock, MapPin, Star } from "lucide-react";
import toast from "react-hot-toast";
import { fetchRestaurantById, fetchRestaurantMenu, fetchRestaurantReviews } from "../../api/restaurants";
import { fetchWishlist, addToWishlist, removeFromWishlist } from "../../api/wishlist";
import { useAuth } from "../../context/AuthContext";
import FoodCard from "../../components/FoodCard";
import LoadingSpinner from "../../components/LoadingSpinner";
import StarRating from "../../components/StarRating";

export default function RestaurantDetailPage() {
  const { id } = useParams();
  const { user } = useAuth();
  const [restaurant, setRestaurant] = useState(null);
  const [menu, setMenu] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [wishlistIds, setWishlistIds] = useState(new Set());
  const [loading, setLoading] = useState(true);
  const [activeCategory, setActiveCategory] = useState("all");

  useEffect(() => {
    setLoading(true);
    const requests = [fetchRestaurantById(id), fetchRestaurantMenu(id), fetchRestaurantReviews(id)];
    Promise.all(requests)
      .then(([restaurantRes, menuRes, reviewsRes]) => {
        setRestaurant(restaurantRes.data);
        setMenu(menuRes.data || []);
        setReviews(reviewsRes.data || []);
      })
      .finally(() => setLoading(false));

    if (user?.role === "ROLE_CUSTOMER") {
      fetchWishlist().then((res) => setWishlistIds(new Set((res.data || []).map((w) => w.foodItemId))));
    }
  }, [id, user]);

  const toggleWishlist = async (food) => {
    try {
      if (wishlistIds.has(food.id)) {
        await removeFromWishlist(food.id);
        setWishlistIds((prev) => {
          const next = new Set(prev);
          next.delete(food.id);
          return next;
        });
      } else {
        await addToWishlist(food.id);
        setWishlistIds((prev) => new Set(prev).add(food.id));
      }
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not update wishlist");
    }
  };

  if (loading) return <LoadingSpinner label="Loading restaurant..." />;
  if (!restaurant) return <p className="p-10 text-center text-slate-500">Restaurant not found.</p>;

  const categories = ["all", ...new Set(menu.map((f) => f.categoryName).filter(Boolean))];
  const filteredMenu = activeCategory === "all" ? menu : menu.filter((f) => f.categoryName === activeCategory);

  return (
    <div>
      <div className="relative h-56 w-full bg-slate-200 md:h-72">
        <img
          src={restaurant.imageUrl || "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=1200&q=70"}
          alt={restaurant.name}
          className="h-full w-full object-cover"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent" />
        <div className="absolute bottom-4 left-4 right-4 text-white md:bottom-6 md:left-8">
          <h1 className="text-2xl font-extrabold md:text-3xl">{restaurant.name}</h1>
          <p className="text-sm text-white/90">{restaurant.cuisineType}</p>
        </div>
      </div>

      <div className="mx-auto max-w-7xl px-4 py-6">
        <div className="mb-6 flex flex-wrap items-center gap-4 rounded-2xl bg-white p-4 shadow-card text-sm">
          <span className="flex items-center gap-1 font-semibold text-green-700">
            <Star size={16} className="fill-green-700" /> {Number(restaurant.averageRating || 0).toFixed(1)} ({restaurant.totalReviews || 0})
          </span>
          <span className="flex items-center gap-1 text-slate-500">
            <Clock size={16} /> {restaurant.estimatedDeliveryMinutes} mins
          </span>
          <span className="flex items-center gap-1 text-slate-500">
            <MapPin size={16} /> {restaurant.address || restaurant.city}
          </span>
          <span className="ml-auto font-medium text-slate-700">Delivery fee: ₹{restaurant.deliveryFee}</span>
          {!restaurant.open && <span className="rounded-full bg-red-50 px-3 py-1 text-xs font-semibold text-red-600">Currently closed</span>}
        </div>

        <div className="mb-5 flex gap-2 overflow-x-auto pb-1">
          {categories.map((c) => (
            <button
              key={c}
              onClick={() => setActiveCategory(c)}
              className={`shrink-0 rounded-full px-4 py-1.5 text-sm font-medium ${
                activeCategory === c ? "bg-brand-500 text-white" : "bg-white text-slate-600 shadow-card"
              }`}
            >
              {c === "all" ? "All items" : c}
            </button>
          ))}
        </div>

        {filteredMenu.length === 0 ? (
          <p className="text-slate-500">No menu items in this category yet.</p>
        ) : (
          <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
            {filteredMenu.map((food) => (
              <FoodCard
                key={food.id}
                food={food}
                onToggleWishlist={user?.role === "ROLE_CUSTOMER" ? toggleWishlist : undefined}
                isWishlisted={wishlistIds.has(food.id)}
              />
            ))}
          </div>
        )}

        <div className="mt-10">
          <h2 className="mb-4 text-xl font-bold text-slate-900">Reviews</h2>
          {reviews.length === 0 ? (
            <p className="text-slate-500">No reviews yet. Be the first to review!</p>
          ) : (
            <div className="space-y-4">
              {reviews.map((review) => (
                <div key={review.id} className="card p-4">
                  <div className="mb-1 flex items-center justify-between">
                    <span className="font-semibold text-slate-800">{review.customerName}</span>
                    <StarRating rating={review.rating} />
                  </div>
                  {review.comment && <p className="text-sm text-slate-500">{review.comment}</p>}
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
