import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { CreditCard, Banknote, Smartphone } from "lucide-react";
import { useCart } from "../../context/CartContext";
import { fetchMyAddresses, addAddress } from "../../api/address";
import { placeOrder } from "../../api/orders";

const PAYMENT_OPTIONS = [
  { value: "CASH_ON_DELIVERY", label: "Cash on Delivery", icon: Banknote },
  { value: "MOCK_CARD", label: "Card (mock)", icon: CreditCard },
  { value: "MOCK_UPI", label: "UPI (mock)", icon: Smartphone },
];

export default function CheckoutPage() {
  const { cart, emptyCart } = useCart();
  const navigate = useNavigate();
  const [addresses, setAddresses] = useState([]);
  const [selectedAddress, setSelectedAddress] = useState("");
  const [newAddress, setNewAddress] = useState("");
  const [paymentMethod, setPaymentMethod] = useState("CASH_ON_DELIVERY");
  const [discountCode, setDiscountCode] = useState("");
  const [placing, setPlacing] = useState(false);

  useEffect(() => {
    fetchMyAddresses().then((res) => {
      setAddresses(res.data || []);
      if (res.data?.length) setSelectedAddress(res.data[0].addressLine);
    });
  }, []);

  const deliveryFeeEstimate = 40;
  const taxEstimate = (cart.subtotal * 0.05).toFixed(2);
  const estimatedTotal = (Number(cart.subtotal) + deliveryFeeEstimate + Number(taxEstimate)).toFixed(2);

  const handlePlaceOrder = async () => {
    const deliveryAddressLine = selectedAddress || newAddress;
    if (!deliveryAddressLine) {
      toast.error("Please provide a delivery address");
      return;
    }

    setPlacing(true);
    try {
      if (!selectedAddress && newAddress) {
        await addAddress({ label: "Home", addressLine: newAddress, isDefault: true });
      }
      const response = await placeOrder({
        deliveryAddressLine,
        paymentMethod,
        discountCode: discountCode || undefined,
      });
      toast.success("Order placed successfully!");
      navigate(`/orders/${response.data.id}`);
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not place order");
    } finally {
      setPlacing(false);
    }
  };

  if (!cart.items || cart.items.length === 0) {
    return <p className="p-10 text-center text-slate-500">Your cart is empty.</p>;
  }

  return (
    <div className="mx-auto max-w-3xl px-4 py-8">
      <h1 className="mb-6 text-2xl font-bold text-slate-900">Checkout</h1>

      <div className="card mb-5 p-5">
        <h2 className="mb-3 font-semibold text-slate-800">Delivery Address</h2>
        {addresses.length > 0 && (
          <div className="mb-3 space-y-2">
            {addresses.map((address) => (
              <label key={address.id} className="flex items-start gap-3 rounded-xl border border-slate-200 p-3 text-sm">
                <input
                  type="radio"
                  name="address"
                  checked={selectedAddress === address.addressLine}
                  onChange={() => setSelectedAddress(address.addressLine)}
                  className="mt-1"
                />
                <span>
                  <strong>{address.label}</strong>
                  <br />
                  {address.addressLine}, {address.city}
                </span>
              </label>
            ))}
          </div>
        )}
        <label className="label">Or enter a new address</label>
        <textarea
          className="input-field"
          rows={2}
          placeholder="House no, street, city, pincode"
          value={newAddress}
          onChange={(e) => {
            setNewAddress(e.target.value);
            setSelectedAddress("");
          }}
        />
      </div>

      <div className="card mb-5 p-5">
        <h2 className="mb-3 font-semibold text-slate-800">Payment Method</h2>
        <div className="grid grid-cols-1 gap-3 sm:grid-cols-3">
          {PAYMENT_OPTIONS.map((option) => (
            <button
              key={option.value}
              onClick={() => setPaymentMethod(option.value)}
              className={`flex flex-col items-center gap-2 rounded-xl border p-4 text-sm font-medium transition ${
                paymentMethod === option.value
                  ? "border-brand-500 bg-brand-50 text-brand-600"
                  : "border-slate-200 text-slate-600 hover:bg-slate-50"
              }`}
            >
              <option.icon size={22} />
              {option.label}
            </button>
          ))}
        </div>
        <p className="mt-3 text-xs text-slate-400">
          This is a mock payment system for demo purposes — no real transactions are processed.
        </p>
      </div>

      <div className="card mb-5 p-5">
        <h2 className="mb-3 font-semibold text-slate-800">Discount Code</h2>
        <input
          className="input-field"
          placeholder="Try FOODGO50 for orders above ₹200"
          value={discountCode}
          onChange={(e) => setDiscountCode(e.target.value)}
        />
      </div>

      <div className="card p-5">
        <h2 className="mb-3 font-semibold text-slate-800">Order Summary</h2>
        <div className="space-y-1.5 text-sm text-slate-600">
          <div className="flex justify-between">
            <span>Subtotal</span>
            <span>₹{cart.subtotal}</span>
          </div>
          <div className="flex justify-between">
            <span>Delivery Fee (estimate)</span>
            <span>₹{deliveryFeeEstimate}</span>
          </div>
          <div className="flex justify-between">
            <span>Tax (estimate, 5%)</span>
            <span>₹{taxEstimate}</span>
          </div>
          <div className="mt-2 flex justify-between border-t border-slate-100 pt-2 text-base font-bold text-slate-900">
            <span>Estimated Total</span>
            <span>₹{estimatedTotal}</span>
          </div>
          <p className="text-xs text-slate-400">Final totals (including any discount) are confirmed after placing the order.</p>
        </div>
        <button onClick={handlePlaceOrder} disabled={placing} className="btn-primary mt-4 w-full">
          {placing ? "Placing order..." : "Place Order"}
        </button>
      </div>
    </div>
  );
}
