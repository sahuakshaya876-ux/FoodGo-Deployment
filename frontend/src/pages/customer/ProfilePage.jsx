import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { Plus, Trash2 } from "lucide-react";
import { useAuth } from "../../context/AuthContext";
import { updateMyProfile } from "../../api/auth";
import { fetchMyAddresses, addAddress, deleteAddress } from "../../api/address";

export default function ProfilePage() {
  const { user, setUser } = useAuth();
  const [form, setForm] = useState({ fullName: user?.fullName || "", phoneNumber: user?.phoneNumber || "" });
  const [saving, setSaving] = useState(false);
  const [addresses, setAddresses] = useState([]);
  const [showAddressForm, setShowAddressForm] = useState(false);
  const [addressForm, setAddressForm] = useState({ label: "", addressLine: "", city: "" });

  useEffect(() => {
    fetchMyAddresses().then((res) => setAddresses(res.data || []));
  }, []);

  const handleProfileSave = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const response = await updateMyProfile(form);
      const updatedUser = { ...user, ...response.data };
      setUser(updatedUser);
      localStorage.setItem("foodgo_user", JSON.stringify(updatedUser));
      toast.success("Profile updated successfully");
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not update profile");
    } finally {
      setSaving(false);
    }
  };

  const handleAddAddress = async (e) => {
    e.preventDefault();
    try {
      const response = await addAddress(addressForm);
      setAddresses((prev) => [...prev, response.data]);
      setShowAddressForm(false);
      setAddressForm({ label: "", addressLine: "", city: "" });
      toast.success("Address added");
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not add address");
    }
  };

  const handleDeleteAddress = async (id) => {
    try {
      await deleteAddress(id);
      setAddresses((prev) => prev.filter((a) => a.id !== id));
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not delete address");
    }
  };

  return (
    <div className="mx-auto max-w-2xl px-4 py-8">
      <h1 className="mb-6 text-2xl font-bold text-slate-900">My Profile</h1>

      <div className="card mb-6 p-6">
        <h2 className="mb-4 font-semibold text-slate-800">Personal Details</h2>
        <form onSubmit={handleProfileSave} className="space-y-4">
          <div>
            <label className="label">Full name</label>
            <input className="input-field" value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} />
          </div>
          <div>
            <label className="label">Email</label>
            <input className="input-field bg-slate-50" value={user?.email} disabled />
          </div>
          <div>
            <label className="label">Phone number</label>
            <input className="input-field" value={form.phoneNumber} onChange={(e) => setForm({ ...form, phoneNumber: e.target.value })} />
          </div>
          <button type="submit" disabled={saving} className="btn-primary">
            {saving ? "Saving..." : "Save changes"}
          </button>
        </form>
      </div>

      <div className="card p-6">
        <div className="mb-4 flex items-center justify-between">
          <h2 className="font-semibold text-slate-800">Saved Addresses</h2>
          <button onClick={() => setShowAddressForm((v) => !v)} className="btn-secondary !px-3 !py-1.5 text-sm">
            <Plus size={16} /> Add
          </button>
        </div>

        {showAddressForm && (
          <form onSubmit={handleAddAddress} className="mb-4 space-y-3 rounded-xl bg-slate-50 p-4">
            <input
              className="input-field"
              placeholder="Label (Home, Work...)"
              required
              value={addressForm.label}
              onChange={(e) => setAddressForm({ ...addressForm, label: e.target.value })}
            />
            <textarea
              className="input-field"
              placeholder="Full address"
              required
              value={addressForm.addressLine}
              onChange={(e) => setAddressForm({ ...addressForm, addressLine: e.target.value })}
            />
            <input
              className="input-field"
              placeholder="City"
              value={addressForm.city}
              onChange={(e) => setAddressForm({ ...addressForm, city: e.target.value })}
            />
            <button type="submit" className="btn-primary !px-4 !py-2 text-sm">
              Save Address
            </button>
          </form>
        )}

        {addresses.length === 0 ? (
          <p className="text-sm text-slate-500">No saved addresses yet.</p>
        ) : (
          <div className="space-y-3">
            {addresses.map((address) => (
              <div key={address.id} className="flex items-start justify-between rounded-xl border border-slate-200 p-3 text-sm">
                <div>
                  <strong>{address.label}</strong>
                  <p className="text-slate-500">{address.addressLine}, {address.city}</p>
                </div>
                <button onClick={() => handleDeleteAddress(address.id)} className="text-slate-400 hover:text-red-500">
                  <Trash2 size={16} />
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
