import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { fetchAllOrdersAdmin, updateOrderStatusAdmin } from "../../api/admin";
import LoadingSpinner from "../../components/LoadingSpinner";
import { ORDER_STATUS_META } from "../../utils/orderStatus";

const ALL_STATUSES = ["PLACED", "CONFIRMED", "PREPARING", "READY_FOR_PICKUP", "OUT_FOR_DELIVERY", "DELIVERED", "CANCELLED"];

export default function AdminOrdersPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  const load = () => {
    fetchAllOrdersAdmin()
      .then((res) => setOrders(res.data || []))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
  }, []);

  const handleStatusChange = async (id, status) => {
    try {
      await updateOrderStatusAdmin(id, status);
      toast.success("Order status updated");
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not update order");
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <div>
      <h1 className="mb-6 text-2xl font-bold text-slate-900">All Orders</h1>
      {orders.length === 0 ? (
        <p className="text-slate-500">No orders yet.</p>
      ) : (
        <div className="card overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead className="border-b border-slate-100 bg-slate-50 text-slate-500">
              <tr>
                <th className="px-4 py-3">Order</th>
                <th className="px-4 py-3">Restaurant</th>
                <th className="px-4 py-3">Total</th>
                <th className="px-4 py-3">Status</th>
                <th className="px-4 py-3">Update</th>
              </tr>
            </thead>
            <tbody>
              {orders.map((order) => (
                <tr key={order.id} className="border-b border-slate-50">
                  <td className="px-4 py-3 font-medium">#{order.id}</td>
                  <td className="px-4 py-3">{order.restaurantName}</td>
                  <td className="px-4 py-3">₹{order.totalAmount}</td>
                  <td className="px-4 py-3">
                    <span className={`rounded-full px-2.5 py-1 text-xs font-semibold ${ORDER_STATUS_META[order.status]?.color}`}>
                      {ORDER_STATUS_META[order.status]?.label}
                    </span>
                  </td>
                  <td className="px-4 py-3">
                    <select
                      value={order.status}
                      onChange={(e) => handleStatusChange(order.id, e.target.value)}
                      className="rounded-lg border border-slate-200 px-2 py-1 text-xs"
                    >
                      {ALL_STATUSES.map((s) => (
                        <option key={s} value={s}>{s.replaceAll("_", " ")}</option>
                      ))}
                    </select>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
