import apiClient from "./client";

export const searchFoods = (params = {}) => apiClient.get("/foods", { params }).then((r) => r.data);
export const fetchFoodById = (id) => apiClient.get(`/foods/${id}`).then((r) => r.data);

export const createFoodItem = (payload) => apiClient.post("/restaurant/menu", payload).then((r) => r.data);
export const updateFoodItem = (id, payload) => apiClient.put(`/restaurant/menu/${id}`, payload).then((r) => r.data);
export const deleteFoodItem = (id) => apiClient.delete(`/restaurant/menu/${id}`).then((r) => r.data);
export const setFoodAvailability = (id, available) =>
  apiClient.put(`/restaurant/menu/${id}/availability?available=${available}`).then((r) => r.data);
