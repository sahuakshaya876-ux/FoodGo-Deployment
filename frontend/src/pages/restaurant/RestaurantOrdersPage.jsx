import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { fetchRestaurantOrders, updateRestaurantOrderStatus } from "../../api/orders";
import LoadingSpinner from "../../components/LoadingSpinner";
import { ORDER_STATUS_FLOW, ORDER_STATUS_META } from "../../utils/orderStatus";

export default function RestaurantOrdersPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  const load = () => {
    fetchRestaurantOrders()
      .then((res) => setOrders(res.data || []))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
  }, []);

  const nextStatuses = (current) => {
    if (current === "CANCELLED" || current === "DELIVERED") return [];
    const idx = ORDER_STATUS_FLOW.indexOf(current);
    return ORDER_STATUS_FLOW.slice(idx + 1, idx + 2).concat("CANCELLED");
  };

  const handleUpdate = async (id, status) => {
    try {
      await updateRestaurantOrderStatus(id, status);
      toast.success(`Order marked as ${status.replaceAll("_", " ")}`);
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not update order");
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <div>
      <h1 className="mb-6 text-2xl font-bold text-slate-900">Incoming Orders</h1>
      {orders.length === 0 ? (
        <p className="text-slate-500">No orders yet.</p>
      ) : (
        <div className="space-y-4">
          {orders.map((order) => (
            <div key={order.id} className="card p-5">
              <div className="mb-3 flex items-center justify-between">
                <div>
                  <h3 className="font-semibold text-slate-900">Order #{order.id}</h3>
                  <p className="text-xs text-slate-400">{new Date(order.createdAt).toLocaleString()}</p>
                </div>
                <span className={`rounded-full px-3 py-1 text-xs font-semibold ${ORDER_STATUS_META[order.status]?.color}`}>
                  {ORDER_STATUS_META[order.status]?.label}
                </span>
              </div>
              <div className="mb-3 space-y-1 text-sm text-slate-600">
                {order.items.map((item) => (
                  <div key={item.id} className="flex justify-between">
                    <span>{item.foodItemName} × {item.quantity}</span>
                    <span>₹{item.lineTotal}</span>
                  </div>
                ))}
              </div>
              <p className="mb-3 text-sm font-semibold text-slate-800">Total: ₹{order.totalAmount}</p>
              <div className="flex flex-wrap gap-2">
                {nextStatuses(order.status).map((status) => (
                  <button
                    key={status}
                    onClick={() => handleUpdate(order.id, status)}
                    className={status === "CANCELLED" ? "btn-secondary !text-red-500 text-sm" : "btn-primary text-sm"}
                  >
                    Mark {status.replaceAll("_", " ")}
                  </button>
                ))}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
