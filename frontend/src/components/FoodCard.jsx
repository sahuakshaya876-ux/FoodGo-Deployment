import { useState } from "react";
import { Plus, Minus, Leaf, Heart } from "lucide-react";
import { useAuth } from "../context/AuthContext";
import { useCart } from "../context/CartContext";
import toast from "react-hot-toast";

export default function FoodCard({ food, onToggleWishlist, isWishlisted }) {
  const { user } = useAuth();
  const { cart, addItem, updateItem, removeItem } = useCart();
  const [busy, setBusy] = useState(false);

  const cartItem = cart.items?.find((item) => item.foodItemId === food.id);

  const handleAdd = async () => {
    if (!user) {
      toast.error("Please login to order");
      return;
    }
    if (user.role !== "ROLE_CUSTOMER") {
      toast.error("Only customer accounts can order food");
      return;
    }
    setBusy(true);
    try {
      await addItem(food.id, 1);
      toast.success(`${food.name} added to cart`);
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not add item");
    } finally {
      setBusy(false);
    }
  };

  const handleQuantityChange = async (delta) => {
    setBusy(true);
    try {
      const newQty = cartItem.quantity + delta;
      if (newQty <= 0) {
        await removeItem(cartItem.id);
      } else {
        await updateItem(cartItem.id, newQty);
      }
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="card flex gap-4 p-4">
      <img
        src={food.imageUrl || "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=300&q=60"}
        alt={food.name}
        className="h-24 w-24 shrink-0 rounded-xl object-cover"
      />
      <div className="flex flex-1 flex-col justify-between">
        <div>
          <div className="flex items-center gap-2">
            <span className={`inline-block h-3.5 w-3.5 rounded-sm border-2 ${food.vegetarian ? "border-green-600" : "border-red-600"}`}>
              <span className={`m-auto block h-1.5 w-1.5 rounded-full ${food.vegetarian ? "bg-green-600" : "bg-red-600"}`} />
            </span>
            <h4 className="font-semibold text-slate-900">{food.name}</h4>
          </div>
          <p className="mt-1 text-sm text-slate-500 line-clamp-2">{food.description}</p>
        </div>
        <div className="mt-2 flex items-center justify-between">
          <span className="font-bold text-slate-900">₹{Number(food.price).toFixed(0)}</span>
          <div className="flex items-center gap-2">
            {onToggleWishlist && (
              <button onClick={() => onToggleWishlist(food)} className="rounded-full p-1.5 hover:bg-slate-100">
                <Heart size={18} className={isWishlisted ? "fill-brand-500 text-brand-500" : "text-slate-400"} />
              </button>
            )}
            {cartItem ? (
              <div className="flex items-center gap-2 rounded-lg bg-brand-500 px-2 py-1 text-white">
                <button disabled={busy} onClick={() => handleQuantityChange(-1)}>
                  <Minus size={14} />
                </button>
                <span className="w-4 text-center text-sm font-semibold">{cartItem.quantity}</span>
                <button disabled={busy} onClick={() => handleQuantityChange(1)}>
                  <Plus size={14} />
                </button>
              </div>
            ) : (
              <button disabled={busy || !food.available} onClick={handleAdd} className="btn-primary !px-3 !py-1.5 text-sm">
                {food.available ? "Add" : "Sold out"}
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
