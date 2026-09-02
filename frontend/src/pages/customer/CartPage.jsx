import { Link, useNavigate } from "react-router-dom";
import { Minus, Plus, Trash2, ShoppingBag } from "lucide-react";
import { useCart } from "../../context/CartContext";
import EmptyState from "../../components/EmptyState";

export default function CartPage() {
  const { cart, updateItem, removeItem, emptyCart, loading } = useCart();
  const navigate = useNavigate();

  if (loading) return <div className="p-10 text-center text-slate-400">Loading cart...</div>;

  if (!cart.items || cart.items.length === 0) {
    return (
      <div className="mx-auto max-w-2xl px-4 py-16">
        <EmptyState
          icon={ShoppingBag}
          title="Your cart is empty"
          description="Looks like you haven't added anything yet. Explore restaurants and find something tasty!"
          action={
            <Link to="/restaurants" className="btn-primary mt-2">
              Browse Restaurants
            </Link>
          }
        />
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-4xl px-4 py-8">
      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-900">Your Cart</h1>
        <button onClick={emptyCart} className="text-sm font-medium text-red-500 hover:underline">
          Clear cart
        </button>
      </div>

      <div className="space-y-4">
        {cart.items.map((item) => (
          <div key={item.id} className="card flex items-center gap-4 p-4">
            <img
              src={item.foodItemImageUrl || "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=200&q=60"}
              alt={item.foodItemName}
              className="h-16 w-16 rounded-lg object-cover"
            />
            <div className="flex-1">
              <h4 className="font-semibold text-slate-900">{item.foodItemName}</h4>
              <p className="text-sm text-slate-500">₹{item.price} each</p>
            </div>
            <div className="flex items-center gap-2 rounded-lg bg-slate-100 px-2 py-1">
              <button onClick={() => updateItem(item.id, item.quantity - 1)} disabled={item.quantity <= 1}>
                <Minus size={14} />
              </button>
              <span className="w-5 text-center text-sm font-semibold">{item.quantity}</span>
              <button onClick={() => updateItem(item.id, item.quantity + 1)}>
                <Plus size={14} />
              </button>
            </div>
            <span className="w-16 text-right font-semibold">₹{item.lineTotal}</span>
            <button onClick={() => removeItem(item.id)} className="text-slate-400 hover:text-red-500">
              <Trash2 size={18} />
            </button>
          </div>
        ))}
      </div>

      <div className="card mt-6 p-5">
        <div className="flex items-center justify-between text-lg font-bold text-slate-900">
          <span>Subtotal</span>
          <span>₹{cart.subtotal}</span>
        </div>
        <p className="mt-1 text-xs text-slate-400">Delivery fee, taxes, and discounts calculated at checkout.</p>
        <button onClick={() => navigate("/checkout")} className="btn-primary mt-4 w-full">
          Proceed to Checkout
        </button>
      </div>
    </div>
  );
}
