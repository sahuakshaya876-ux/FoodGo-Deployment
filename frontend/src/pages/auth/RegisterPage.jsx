import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { useAuth } from "../../context/AuthContext";

export default function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    fullName: "",
    email: "",
    password: "",
    phoneNumber: "",
    role: "ROLE_CUSTOMER",
  });
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const user = await register(form);
      toast.success(`Account created! Welcome, ${user.fullName.split(" ")[0]}`);
      navigate(user.role === "ROLE_RESTAURANT_OWNER" ? "/restaurant/dashboard" : "/");
    } catch (err) {
      toast.error(err.response?.data?.message || "Registration failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="mx-auto flex min-h-[80vh] max-w-md flex-col justify-center px-4 py-12">
      <div className="card p-8">
        <h1 className="mb-1 text-2xl font-bold text-slate-900">Create your account</h1>
        <p className="mb-6 text-sm text-slate-500">Join FoodGo to order or sell delicious food.</p>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="label">Full name</label>
            <input
              required
              className="input-field"
              value={form.fullName}
              onChange={(e) => setForm({ ...form, fullName: e.target.value })}
              placeholder="Jane Doe"
            />
          </div>
          <div>
            <label className="label">Email</label>
            <input
              type="email"
              required
              className="input-field"
              value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })}
              placeholder="you@example.com"
            />
          </div>
          <div>
            <label className="label">Phone number</label>
            <input
              className="input-field"
              value={form.phoneNumber}
              onChange={(e) => setForm({ ...form, phoneNumber: e.target.value })}
              placeholder="+91 90000 00000"
            />
          </div>
          <div>
            <label className="label">Password</label>
            <input
              type="password"
              required
              minLength={6}
              className="input-field"
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
              placeholder="At least 6 characters"
            />
          </div>
          <div>
            <label className="label">I want to</label>
            <div className="grid grid-cols-2 gap-3">
              {[
                { value: "ROLE_CUSTOMER", label: "Order food" },
                { value: "ROLE_RESTAURANT_OWNER", label: "Sell food" },
              ].map((option) => (
                <button
                  type="button"
                  key={option.value}
                  onClick={() => setForm({ ...form, role: option.value })}
                  className={`rounded-xl border px-4 py-3 text-sm font-medium transition ${
                    form.role === option.value
                      ? "border-brand-500 bg-brand-50 text-brand-600"
                      : "border-slate-200 text-slate-600 hover:bg-slate-50"
                  }`}
                >
                  {option.label}
                </button>
              ))}
            </div>
          </div>
          <button type="submit" disabled={loading} className="btn-primary w-full">
            {loading ? "Creating account..." : "Create account"}
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-slate-500">
          Already have an account?{" "}
          <Link to="/login" className="font-semibold text-brand-500">
            Login
          </Link>
        </p>
      </div>
    </div>
  );
}
