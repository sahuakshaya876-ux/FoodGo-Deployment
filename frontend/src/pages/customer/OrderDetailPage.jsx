import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import toast from "react-hot-toast";
import { CheckCircle2, XCircle } from "lucide-react";
import { fetchMyOrderById, cancelOrder } from "../../api/orders";
import { submitReview } from "../../api/reviews";
import LoadingSpinner from "../../components/LoadingSpinner";
import { ORDER_STATUS_FLOW, ORDER_STATUS_META } from "../../utils/orderStatus";

export default function OrderDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState("");
  const [submittingReview, setSubmittingReview] = useState(false);

  const load = () => {
    fetchMyOrderById(id)
      .then((res) => setOrder(res.data))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  const handleCancel = async () => {
    try {
      await cancelOrder(id);
      toast.success("Order cancelled");
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not cancel order");
    }
  };

  const handleReviewSubmit = async (e) => {
    e.preventDefault();
    setSubmittingReview(true);
    try {
      await submitReview({ restaurantId: order.restaurantId, rating, comment });
      toast.success("Thanks for your feedback!");
      setComment("");
    } catch (err) {
      toast.error(err.response?.data?.message || "Could not submit review");
    } finally {
      setSubmittingReview(false);
    }
  };

  if (loading) return <LoadingSpinner label="Loading order..." />;
  if (!order) return <p className="p-10 text-center text-slate-500">Order not found.</p>;

  const currentStepIndex = ORDER_STATUS_FLOW.indexOf(order.status);
  const isCancelled = order.status === "CANCELLED";
  const canCancel = order.status === "PLACED" || order.status === "CONFIRMED";

  return (
    <div className="mx-auto max-w-3xl px-4 py-8">
      <button onClick={() => navigate("/orders")} className="mb-4 text-sm text-slate-500 hover:underline">
        &larr; Back to orders
      </button>

      <div className="card mb-6 p-6">
        <div className="mb-4 flex items-center justify-between">
          <div>
            <h1 className="text-xl font-bold text-slate-900">{order.restaurantName}</h1>
            <p className="text-xs text-slate-400">Order #{order.id} • {new Date(order.createdAt).toLocaleString()}</p>
          </div>
          <span className={`rounded-full px-3 py-1 text-xs font-semibold ${ORDER_STATUS_META[order.status]?.color}`}>
            {ORDER_STATUS_META[order.status]?.label}
          </span>
        </div>

        {!isCancelled ? (
          <div className="mb-6 flex items-center justify-between">
            {ORDER_STATUS_FLOW.map((status, index) => (
              <div key={status} className="flex flex-1 flex-col items-center text-center">
                <div
                  className={`mb-1 flex h-7 w-7 items-center justify-center rounded-full text-xs font-bold ${
                    index <= currentStepIndex ? "bg-brand-500 text-white" : "bg-slate-200 text-slate-400"
                  }`}
                >
                  {index <= currentStepIndex ? <CheckCircle2 size={16} /> : index + 1}
                </div>
                <span className="hidden text-[10px] text-slate-500 sm:block">{ORDER_STATUS_META[status].label}</span>
                {index < ORDER_STATUS_FLOW.length - 1 && (
                  <div className={`absolute mt-3.5 h-0.5 w-full ${index < currentStepIndex ? "bg-brand-500" : "bg-slate-200"}`} />
                )}
              </div>
            ))}
          </div>
        ) : (
          <div className="mb-6 flex items-center gap-2 rounded-xl bg-red-50 p-3 text-sm text-red-600">
            <XCircle size={18} /> This order was cancelled.
          </div>
        )}

        <div className="mb-4 space-y-2 border-t border-slate-100 pt-4">
          {order.items.map((item) => (
            <div key={item.id} className="flex justify-between text-sm">
              <span>
                {item.foodItemName} × {item.quantity}
              </span>
              <span>₹{item.lineTotal}</span>
            </div>
          ))}
        </div>

        <div className="space-y-1 border-t border-slate-100 pt-4 text-sm text-slate-600">
          <div className="flex justify-between"><span>Subtotal</span><span>₹{order.subtotal}</span></div>
          <div className="flex justify-between"><span>Delivery Fee</span><span>₹{order.deliveryFee}</span></div>
          <div className="flex justify-between"><span>Tax</span><span>₹{order.tax}</span></div>
          {Number(order.discount) > 0 && (
            <div className="flex justify-between text-green-600"><span>Discount</span><span>-₹{order.discount}</span></div>
          )}
          <div className="flex justify-between border-t border-slate-100 pt-2 text-base font-bold text-slate-900">
            <span>Total</span><span>₹{order.totalAmount}</span>
          </div>
        </div>

        <div className="mt-4 rounded-xl bg-slate-50 p-3 text-sm text-slate-600">
          <p><strong>Deliver to:</strong> {order.deliveryAddressLine}</p>
          {order.payment && (
            <p className="mt-1">
              <strong>Payment:</strong> {order.payment.method.replaceAll("_", " ")} — {order.payment.status}
            </p>
          )}
        </div>

        {canCancel && (
          <button onClick={handleCancel} className="btn-secondary mt-4 w-full !text-red-500">
            Cancel Order
          </button>
        )}
      </div>

      {order.status === "DELIVERED" && (
        <div className="card p-6">
          <h2 className="mb-3 font-semibold text-slate-800">Rate your experience</h2>
          <form onSubmit={handleReviewSubmit} className="space-y-3">
            <div className="flex gap-1">
              {[1, 2, 3, 4, 5].map((n) => (
                <button type="button" key={n} onClick={() => setRating(n)} className="text-2xl">
                  {n <= rating ? "⭐" : "☆"}
                </button>
              ))}
            </div>
            <textarea
              className="input-field"
              rows={3}
              placeholder="Tell us about your experience..."
              value={comment}
              onChange={(e) => setComment(e.target.value)}
            />
            <button type="submit" disabled={submittingReview} className="btn-primary">
              {submittingReview ? "Submitting..." : "Submit Review"}
            </button>
          </form>
        </div>
      )}
    </div>
  );
}
