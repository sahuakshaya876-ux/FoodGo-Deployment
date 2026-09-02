import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Plus, Pencil, Trash2 } from "lucide-react";
import { getMyRestaurant } from "../../api/restaurants";
import { fetchCategories } from "../../api/categories";
import { createFoodItem, updateFoodItem, deleteFoodItem, setFoodAvailability } from "../../api/foods";
import apiClient from "../../api/client";
import LoadingSpinner from "../../components/LoadingSpinner";

const emptyForm = { name: "", description: "", price: "", imageUrl: "", categoryId: "", vegetarian: true, available: true };

export default function RestaurantMenuPage() {
  const [restaurant, setRestaurant] = useState(null);
  const [menu, setMenu] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState(emptyForm);

  const load = async () => {
    setLoading(true);
    try {
      const restaurantRes = await getMyRestaurant();
      setRestaurant(restaurantRes.data);
      const [menuRes, categoriesRes] = await Promise.all([
        apiClient.get(`/restaurants/${restaurantRes.data.id}/foods`),
        fetchCategories(),
      ]);
      setMenu(menuRes.data.data || []);
      setCategories(categoriesRes.data || []);
    } catch (err) {
      toast.error("Please register your restaurant first");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const resetForm = () => {
    setForm(emptyForm);
    setEditingId(null);
    setShowForm(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = { ...form, price: Number(form.price), categoryId: form.categoryId || null };
      if (editingId) {
        await updateFoodItem(editingId, payload);
        toast.success("Food item updated");
      } else {
        await createFoodItem(payload);
        toast.success("Food item added");
      }
      resetForm();
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not save food item");
    }
  };

  const handleEdit = (food) => {
    setForm({
      name: food.name,
      description: food.description || "",
      price: food.price,
      imageUrl: food.imageUrl || "",
      categoryId: food.categoryId || "",
      vegetarian: food.vegetarian,
      available: food.available,
    });
    setEditingId(food.id);
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    try {
      await deleteFoodItem(id);
      toast.success("Food item removed");
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not delete food item");
    }
  };

  const toggleAvailability = async (food) => {
    try {
      await setFoodAvailability(food.id, !food.available);
      load();
    } catch (err) {
      toast.error("Could not update availability");
    }
  };

  if (loading) return <LoadingSpinner />;
  if (!restaurant) return <p className="text-slate-500">Register your restaurant first from the Restaurant Profile tab.</p>;

  return (
    <div>
      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-900">Menu Management</h1>
        <button onClick={() => setShowForm((v) => !v)} className="btn-primary !px-4 !py-2 text-sm">
          <Plus size={16} /> Add Food Item
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="card mb-6 space-y-4 p-6">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="label">Name</label>
              <input required className="input-field" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
            </div>
            <div>
              <label className="label">Price (₹)</label>
              <input required type="number" min="1" step="0.01" className="input-field" value={form.price} onChange={(e) => setForm({ ...form, price: e.target.value })} />
            </div>
          </div>
          <div>
            <label className="label">Description</label>
            <textarea className="input-field" rows={2} value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="label">Category</label>
              <select className="input-field" value={form.categoryId} onChange={(e) => setForm({ ...form, categoryId: e.target.value })}>
                <option value="">None</option>
                {categories.map((c) => (
                  <option key={c.id} value={c.id}>{c.name}</option>
                ))}
              </select>
            </div>
            <div>
              <label className="label">Image URL</label>
              <input className="input-field" value={form.imageUrl} onChange={(e) => setForm({ ...form, imageUrl: e.target.value })} />
            </div>
          </div>
          <label className="flex items-center gap-2 text-sm">
            <input type="checkbox" checked={form.vegetarian} onChange={(e) => setForm({ ...form, vegetarian: e.target.checked })} />
            Vegetarian
          </label>
          <div className="flex gap-3">
            <button type="submit" className="btn-primary">{editingId ? "Update" : "Add"} Item</button>
            <button type="button" onClick={resetForm} className="btn-secondary">Cancel</button>
          </div>
        </form>
      )}

      {menu.length === 0 ? (
        <p className="text-slate-500">No food items yet. Add your first dish!</p>
      ) : (
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
          {menu.map((food) => (
            <div key={food.id} className="card flex items-center gap-4 p-4">
              <img
                src={food.imageUrl || "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=150&q=60"}
                alt={food.name}
                className="h-16 w-16 rounded-lg object-cover"
              />
              <div className="flex-1">
                <h4 className="font-semibold text-slate-900">{food.name}</h4>
                <p className="text-sm text-slate-500">₹{food.price}</p>
                <button onClick={() => toggleAvailability(food)} className={`mt-1 text-xs font-semibold ${food.available ? "text-green-600" : "text-red-500"}`}>
                  {food.available ? "Available" : "Unavailable"} (toggle)
                </button>
              </div>
              <div className="flex flex-col gap-2">
                <button onClick={() => handleEdit(food)} className="rounded-lg p-1.5 text-slate-500 hover:bg-slate-100">
                  <Pencil size={16} />
                </button>
                <button onClick={() => handleDelete(food.id)} className="rounded-lg p-1.5 text-red-500 hover:bg-red-50">
                  <Trash2 size={16} />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
