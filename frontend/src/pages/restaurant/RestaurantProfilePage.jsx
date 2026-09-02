import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { getMyRestaurant, updateMyRestaurant, registerRestaurant, setRestaurantAvailability } from "../../api/restaurants";
import LoadingSpinner from "../../components/LoadingSpinner";

const emptyForm = {
  name: "",
  description: "",
  cuisineType: "",
  imageUrl: "",
  address: "",
  city: "",
  deliveryFee: 40,
  estimatedDeliveryMinutes: 35,
};

export default function RestaurantProfilePage() {
  const [form, setForm] = useState(emptyForm);
  const [restaurant, setRestaurant] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    getMyRestaurant()
      .then((res) => {
        setRestaurant(res.data);
        setForm(res.data);
      })
      .catch(() => setRestaurant(null))
      .finally(() => setLoading(false));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const response = restaurant ? await updateMyRestaurant(form) : await registerRestaurant(form);
      setRestaurant(response.data);
      toast.success(restaurant ? "Restaurant updated" : "Restaurant registered! Awaiting admin approval.");
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not save restaurant");
    } finally {
      setSaving(false);
    }
  };

  const toggleAvailability = async () => {
    try {
      const response = await setRestaurantAvailability(!restaurant.open);
      setRestaurant(response.data);
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not update availability");
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <div className="max-w-2xl">
      <h1 className="mb-6 text-2xl font-bold text-slate-900">Restaurant Profile</h1>

      {restaurant && (
        <div className="card mb-6 flex items-center justify-between p-4">
          <div>
            <p className="text-sm text-slate-500">
              Status: <strong>{restaurant.status.replaceAll("_", " ")}</strong>
            </p>
            <p className="text-sm text-slate-500">Currently: <strong>{restaurant.open ? "Open" : "Closed"}</strong></p>
          </div>
          <button onClick={toggleAvailability} className="btn-secondary">
            {restaurant.open ? "Mark Closed" : "Mark Open"}
          </button>
        </div>
      )}

      <form onSubmit={handleSubmit} className="card space-y-4 p-6">
        <div>
          <label className="label">Restaurant name</label>
          <input required className="input-field" value={form.name || ""} onChange={(e) => setForm({ ...form, name: e.target.value })} />
        </div>
        <div>
          <label className="label">Description</label>
          <textarea className="input-field" rows={3} value={form.description || ""} onChange={(e) => setForm({ ...form, description: e.target.value })} />
        </div>
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="label">Cuisine type</label>
            <input className="input-field" value={form.cuisineType || ""} onChange={(e) => setForm({ ...form, cuisineType: e.target.value })} />
          </div>
          <div>
            <label className="label">City</label>
            <input className="input-field" value={form.city || ""} onChange={(e) => setForm({ ...form, city: e.target.value })} />
          </div>
        </div>
        <div>
          <label className="label">Address</label>
          <input className="input-field" value={form.address || ""} onChange={(e) => setForm({ ...form, address: e.target.value })} />
        </div>
        <div>
          <label className="label">Image URL</label>
          <input className="input-field" value={form.imageUrl || ""} onChange={(e) => setForm({ ...form, imageUrl: e.target.value })} />
        </div>
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="label">Delivery fee (₹)</label>
            <input type="number" className="input-field" value={form.deliveryFee || 0} onChange={(e) => setForm({ ...form, deliveryFee: e.target.value })} />
          </div>
          <div>
            <label className="label">Est. delivery time (mins)</label>
            <input type="number" className="input-field" value={form.estimatedDeliveryMinutes || 0} onChange={(e) => setForm({ ...form, estimatedDeliveryMinutes: e.target.value })} />
          </div>
        </div>
        <button type="submit" disabled={saving} className="btn-primary">
          {saving ? "Saving..." : restaurant ? "Save changes" : "Register Restaurant"}
        </button>
      </form>
    </div>
  );
}
