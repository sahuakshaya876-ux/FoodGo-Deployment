import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { PackageSearch } from "lucide-react";
import { fetchMyOrders } from "../../api/orders";
import LoadingSpinner from "../../components/LoadingSpinner";
import EmptyState from "../../components/EmptyState";
import { ORDER_STATUS_META } from "../../utils/orderStatus";

export default function OrdersPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchMyOrders()
      .then((res) => setOrders(res.data || []))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <LoadingSpinner label="Loading your orders..." />;

  if (orders.length === 0) {
    return (
      <div className="mx-auto max-w-2xl px-4 py-16">
        <EmptyState icon={PackageSearch} title="No orders yet" description="Once you place an order, it will show up here." />
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-4xl px-4 py-8">
      <h1 className="mb-6 text-2xl font-bold text-slate-900">My Orders</h1>
      <div className="space-y-4">
        {orders.map((order) => {
          const meta = ORDER_STATUS_META[order.status] || {};
          return (
            <Link to={`/orders/${order.id}`} key={order.id} className="card block p-4">
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="font-semibold text-slate-900">{order.restaurantName}</h3>
                  <p className="text-xs text-slate-400">
                    {new Date(order.createdAt).toLocaleString()} • {order.items?.length} items
                  </p>
                </div>
                <div className="text-right">
                  <span className={`inline-block rounded-full px-3 py-1 text-xs font-semibold ${meta.color}`}>
                    {meta.label}
                  </span>
                  <p className="mt-1 font-bold text-slate-900">₹{order.totalAmount}</p>
                </div>
              </div>
            </Link>
          );
        })}
      </div>
    </div>
  );
}
