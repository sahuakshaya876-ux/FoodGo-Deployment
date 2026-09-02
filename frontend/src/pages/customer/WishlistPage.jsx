import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Heart, Trash2 } from "lucide-react";
import toast from "react-hot-toast";
import { fetchWishlist, removeFromWishlist } from "../../api/wishlist";
import LoadingSpinner from "../../components/LoadingSpinner";
import EmptyState from "../../components/EmptyState";

export default function WishlistPage() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  const load = () => {
    fetchWishlist()
      .then((res) => setItems(res.data || []))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
  }, []);

  const handleRemove = async (foodItemId) => {
    try {
      await removeFromWishlist(foodItemId);
      setItems((prev) => prev.filter((item) => item.foodItemId !== foodItemId));
      toast.success("Removed from wishlist");
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not remove item");
    }
  };

  if (loading) return <LoadingSpinner label="Loading your wishlist..." />;

  if (items.length === 0) {
    return (
      <div className="mx-auto max-w-2xl px-4 py-16">
        <EmptyState icon={Heart} title="Your wishlist is empty" description="Save your favorite dishes here for quick access later." />
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-4xl px-4 py-8">
      <h1 className="mb-6 text-2xl font-bold text-slate-900">My Wishlist</h1>
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        {items.map((item) => (
          <div key={item.id} className="card flex items-center gap-4 p-4">
            <img
              src={item.foodItemImageUrl || "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=200&q=60"}
              alt={item.foodItemName}
              className="h-16 w-16 rounded-lg object-cover"
            />
            <div className="flex-1">
              <Link to={`/restaurants/${item.restaurantId}`} className="font-semibold text-slate-900 hover:text-brand-500">
                {item.foodItemName}
              </Link>
              <p className="text-sm text-slate-500">{item.restaurantName} • ₹{item.price}</p>
            </div>
            <button onClick={() => handleRemove(item.foodItemId)} className="text-slate-400 hover:text-red-500">
              <Trash2 size={18} />
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
