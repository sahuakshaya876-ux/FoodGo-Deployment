import apiClient from "./client";

export const fetchDashboardStats = () => apiClient.get("/admin/dashboard").then((r) => r.data);
export const fetchAllUsers = () => apiClient.get("/admin/users").then((r) => r.data);
export const updateUserStatus = (id, enabled) =>
  apiClient.put(`/admin/users/${id}/status`, { enabled }).then((r) => r.data);
export const fetchAllRestaurantsAdmin = () => apiClient.get("/admin/restaurants").then((r) => r.data);
export const updateRestaurantStatusAdmin = (id, status) =>
  apiClient.put(`/admin/restaurants/${id}/status`, { status }).then((r) => r.data);
export const fetchAllOrdersAdmin = () => apiClient.get("/admin/orders").then((r) => r.data);
export const updateOrderStatusAdmin = (id, status) =>
  apiClient.put(`/admin/orders/${id}/status`, { status }).then((r) => r.data);
