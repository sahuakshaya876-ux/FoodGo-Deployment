import { useEffect, useState } from "react";
import { Users, Store, ClipboardList, DollarSign } from "lucide-react";
import { fetchDashboardStats } from "../../api/admin";
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

export default function AdminDashboardPage() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardStats()
      .then((res) => setStats(res.data))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <LoadingSpinner />;
  if (!stats) return <p className="text-slate-500">Could not load dashboard stats.</p>;

  return (
    <div>
      <h1 className="mb-6 text-2xl font-bold text-slate-900">Admin Dashboard</h1>
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard icon={Users} label="Total Users" value={stats.totalUsers} color="bg-indigo-500" />
        <StatCard icon={Store} label="Total Restaurants" value={stats.totalRestaurants} color="bg-brand-500" />
        <StatCard icon={ClipboardList} label="Total Orders" value={stats.totalOrders} color="bg-cyan-500" />
        <StatCard icon={DollarSign} label="Total Revenue" value={`₹${stats.totalRevenue}`} color="bg-green-500" />
      </div>

      <div className="mt-6 grid grid-cols-1 gap-4 sm:grid-cols-2">
        <div className="card p-5">
          <h2 className="mb-3 font-semibold text-slate-800">User Breakdown</h2>
          <div className="space-y-2 text-sm">
            <div className="flex justify-between"><span>Customers</span><span className="font-semibold">{stats.totalCustomers}</span></div>
            <div className="flex justify-between"><span>Restaurant Owners</span><span className="font-semibold">{stats.totalRestaurantOwners}</span></div>
          </div>
        </div>
        <div className="card p-5">
          <h2 className="mb-3 font-semibold text-slate-800">Order Breakdown</h2>
          <div className="space-y-2 text-sm">
            <div className="flex justify-between"><span>Pending Restaurant Approvals</span><span className="font-semibold">{stats.pendingRestaurantApprovals}</span></div>
            <div className="flex justify-between"><span>Delivered Orders</span><span className="font-semibold">{stats.deliveredOrders}</span></div>
            <div className="flex justify-between"><span>Cancelled Orders</span><span className="font-semibold">{stats.cancelledOrders}</span></div>
          </div>
        </div>
      </div>
    </div>
  );
}
