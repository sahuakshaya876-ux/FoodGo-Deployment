import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { fetchAllUsers, updateUserStatus } from "../../api/admin";
import LoadingSpinner from "../../components/LoadingSpinner";

export default function AdminUsersPage() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState("ALL");

  const load = () => {
    fetchAllUsers()
      .then((res) => setUsers(res.data || []))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
  }, []);

  const handleToggle = async (user) => {
    try {
      await updateUserStatus(user.id, !user.enabled);
      toast.success(`User ${user.enabled ? "disabled" : "enabled"}`);
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not update user");
    }
  };

  const filtered = filter === "ALL" ? users : users.filter((u) => u.role === filter);

  if (loading) return <LoadingSpinner />;

  return (
    <div>
      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-900">Manage Users</h1>
        <select value={filter} onChange={(e) => setFilter(e.target.value)} className="rounded-lg border border-slate-200 px-3 py-2 text-sm">
          <option value="ALL">All roles</option>
          <option value="ROLE_CUSTOMER">Customers</option>
          <option value="ROLE_RESTAURANT_OWNER">Restaurant Owners</option>
          <option value="ROLE_ADMIN">Admins</option>
        </select>
      </div>

      <div className="card overflow-x-auto">
        <table className="w-full text-left text-sm">
          <thead className="border-b border-slate-100 bg-slate-50 text-slate-500">
            <tr>
              <th className="px-4 py-3">Name</th>
              <th className="px-4 py-3">Email</th>
              <th className="px-4 py-3">Role</th>
              <th className="px-4 py-3">Status</th>
              <th className="px-4 py-3">Action</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map((user) => (
              <tr key={user.id} className="border-b border-slate-50">
                <td className="px-4 py-3 font-medium">{user.fullName}</td>
                <td className="px-4 py-3 text-slate-500">{user.email}</td>
                <td className="px-4 py-3">{user.role.replace("ROLE_", "")}</td>
                <td className="px-4 py-3">
                  <span className={`rounded-full px-2.5 py-1 text-xs font-semibold ${user.enabled ? "bg-green-50 text-green-600" : "bg-red-50 text-red-600"}`}>
                    {user.enabled ? "Active" : "Disabled"}
                  </span>
                </td>
                <td className="px-4 py-3">
                  <button onClick={() => handleToggle(user)} className="text-sm font-semibold text-brand-500 hover:underline">
                    {user.enabled ? "Disable" : "Enable"}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
