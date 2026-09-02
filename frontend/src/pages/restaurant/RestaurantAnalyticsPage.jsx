import { useEffect, useMemo, useState } from "react";
import { fetchRestaurantOrders } from "../../api/orders";
import LoadingSpinner from "../../components/LoadingSpinner";

export default function RestaurantAnalyticsPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchRestaurantOrders()
      .then((res) => setOrders(res.data || []))
      .finally(() => setLoading(false));
  }, []);

  const salesByDay = useMemo(() => {
    const map = {};
    orders
      .filter((o) => o.status === "DELIVERED")
      .forEach((o) => {
        const day = new Date(o.createdAt).toLocaleDateString();
        map[day] = (map[day] || 0) + Number(o.totalAmount);
      });
    return Object.entries(map).sort((a, b) => new Date(a[0]) - new Date(b[0]));
  }, [orders]);

  const maxSale = Math.max(1, ...salesByDay.map(([, v]) => v));

  if (loading) return <LoadingSpinner />;

  return (
    <div>
      <h1 className="mb-6 text-2xl font-bold text-slate-900">Sales Analytics</h1>
      <div className="card p-6">
        <h2 className="mb-4 font-semibold text-slate-800">Revenue by Day (delivered orders)</h2>
        {salesByDay.length === 0 ? (
          <p className="text-sm text-slate-500">No delivered orders yet.</p>
        ) : (
          <div className="space-y-3">
            {salesByDay.map(([day, total]) => (
              <div key={day} className="flex items-center gap-3">
                <span className="w-24 shrink-0 text-xs text-slate-500">{day}</span>
                <div className="h-4 flex-1 rounded-full bg-slate-100">
                  <div
                    className="h-4 rounded-full bg-brand-500"
                    style={{ width: `${(total / maxSale) * 100}%` }}
                  />
                </div>
                <span className="w-20 shrink-0 text-right text-sm font-semibold">₹{total.toFixed(0)}</span>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
