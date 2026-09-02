import { useEffect, useMemo, useState } from "react";
import { ClipboardList, DollarSign, Clock, CheckCircle2 } from "lucide-react";
import { fetchRestaurantOrders } from "../../api/orders";
import LoadingSpinner from "../../components/LoadingSpinner";

function StatCard({ icon: Icon, label, value, color }) {
  return (
    <div className="card flex items-center gap-4 p-5">
      <div className={`flex h-12 w-12 items-center justify-center rounded-xl ${color}`}>
        <Icon size={22} className="text-white" />
      </div>
      <div>
        <p className="text-sm text-slate-500">{label}</p>
        <p className="text-xl font-bold text-slate-900">{value}</p>
      </div>
    </div>
  );
}

export default function RestaurantDashboardPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchRestaurantOrders()
      .then((res) => setOrders(res.data || []))
      .finally(() => setLoading(false));
  }, []);

  const stats = useMemo(() => {
    const today = new Date().toDateString();
    const todaysOrders = orders.filter((o) => new Date(o.createdAt).toDateString() === today);
    const completed = orders.filter((o) => o.status === "DELIVERED");
    const pending = orders.filter((o) => !["DELIVERED", "CANCELLED"].includes(o.status));
    const revenue = completed.reduce((sum, o) => sum + Number(o.totalAmount), 0);

    const itemCounts = {};
    orders.forEach((o) =>
      o.items.forEach((item) => {
        itemCounts[item.foodItemName] = (itemCounts[item.foodItemName] || 0) + item.quantity;
      })
    );
    const popularItems = Object.entries(itemCounts)
      .sort((a, b) => b[1] - a[1])
      .slice(0, 5);

    return {
      total: orders.length,
      today: todaysOrders.length,
      revenue: revenue.toFixed(2),
      pending: pending.length,
      completed: completed.length,
      popularItems,
    };
  }, [orders]);

  if (loading) return <LoadingSpinner label="Loading dashboard..." />;

  return (
    <div>
      <h1 className="mb-6 text-2xl font-bold text-slate-900">Restaurant Dashboard</h1>
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard icon={ClipboardList} label="Total Orders" value={stats.total} color="bg-brand-500" />
        <StatCard icon={Clock} label="Today's Orders" value={stats.today} color="bg-indigo-500" />
        <StatCard icon={DollarSign} label="Total Revenue" value={`₹${stats.revenue}`} color="bg-green-500" />
        <StatCard icon={CheckCircle2} label="Pending Orders" value={stats.pending} color="bg-amber-500" />
      </div>

      <div className="card mt-6 p-6">
        <h2 className="mb-4 font-semibold text-slate-800">Popular Food Items</h2>
        {stats.popularItems.length === 0 ? (
          <p className="text-sm text-slate-500">No orders yet.</p>
        ) : (
          <div className="space-y-2">
            {stats.popularItems.map(([name, count]) => (
              <div key={name} className="flex items-center justify-between text-sm">
                <span>{name}</span>
                <span className="font-semibold text-slate-700">{count} sold</span>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
