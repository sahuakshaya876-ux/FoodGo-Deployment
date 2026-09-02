import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Plus, Trash2 } from "lucide-react";
import { fetchCategories, createCategory, deleteCategory } from "../../api/categories";
import LoadingSpinner from "../../components/LoadingSpinner";

export default function AdminCategoriesPage() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({ name: "", imageUrl: "", description: "" });
  const [showForm, setShowForm] = useState(false);

  const load = () => {
    fetchCategories()
      .then((res) => setCategories(res.data || []))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createCategory(form);
      toast.success("Category created");
      setForm({ name: "", imageUrl: "", description: "" });
      setShowForm(false);
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not create category");
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteCategory(id);
      toast.success("Category removed");
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not delete category");
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <div>
      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-900">Food Categories</h1>
        <button onClick={() => setShowForm((v) => !v)} className="btn-primary !px-4 !py-2 text-sm">
          <Plus size={16} /> Add Category
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="card mb-6 space-y-3 p-6">
          <input required className="input-field" placeholder="Category name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
          <input className="input-field" placeholder="Image URL" value={form.imageUrl} onChange={(e) => setForm({ ...form, imageUrl: e.target.value })} />
          <textarea className="input-field" placeholder="Description" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
          <button type="submit" className="btn-primary">Create</button>
        </form>
      )}

      <div className="grid grid-cols-2 gap-4 sm:grid-cols-4">
        {categories.map((category) => (
          <div key={category.id} className="card flex flex-col items-center gap-2 p-4 text-center">
            <img
              src={category.imageUrl || "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=150&q=60"}
              alt={category.name}
              className="h-16 w-16 rounded-full object-cover"
            />
            <span className="text-sm font-medium">{category.name}</span>
            <button onClick={() => handleDelete(category.id)} className="text-slate-400 hover:text-red-500">
              <Trash2 size={14} />
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
