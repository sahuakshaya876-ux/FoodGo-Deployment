import apiClient from "./client";

export const placeOrder = (payload) => apiClient.post("/orders", payload).then((r) => r.data);
export const fetchMyOrders = () => apiClient.get("/orders").then((r) => r.data);
export const fetchMyOrderById = (id) => apiClient.get(`/orders/${id}`).then((r) => r.data);
export const cancelOrder = (id) => apiClient.delete(`/orders/${id}`).then((r) => r.data);

export const fetchRestaurantOrders = () => apiClient.get("/restaurant/orders").then((r) => r.data);
export const updateRestaurantOrderStatus = (id, status) =>
  apiClient.put(`/restaurant/orders/${id}/status`, { status }).then((r) => r.data);
