export const ORDER_STATUS_META = {
  PLACED: { label: "Placed", color: "bg-blue-50 text-blue-600" },
  CONFIRMED: { label: "Confirmed", color: "bg-indigo-50 text-indigo-600" },
  PREPARING: { label: "Preparing", color: "bg-amber-50 text-amber-600" },
  READY_FOR_PICKUP: { label: "Ready for Pickup", color: "bg-purple-50 text-purple-600" },
  OUT_FOR_DELIVERY: { label: "Out for Delivery", color: "bg-cyan-50 text-cyan-600" },
  DELIVERED: { label: "Delivered", color: "bg-green-50 text-green-600" },
  CANCELLED: { label: "Cancelled", color: "bg-red-50 text-red-600" },
};

export const ORDER_STATUS_FLOW = [
  "PLACED",
  "CONFIRMED",
  "PREPARING",
  "READY_FOR_PICKUP",
  "OUT_FOR_DELIVERY",
  "DELIVERED",
];
