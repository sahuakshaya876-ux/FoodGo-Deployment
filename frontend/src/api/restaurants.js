import apiClient from "./client";

export const fetchRestaurants = (params = {}) =>
  apiClient.get("/restaurants", { params }).then((r) => r.data);

export const fetchRestaurantById = (id) => apiClient.get(`/restaurants/${id}`).then((r) => r.data);

export const fetchRestaurantMenu = (restaurantId) =>
  apiClient.get(`/restaurants/${restaurantId}/foods`).then((r) => r.data);

export const fetchRestaurantReviews = (restaurantId) =>
  apiClient.get(`/restaurants/${restaurantId}/reviews`).then((r) => r.data);

export const registerRestaurant = (payload) => apiClient.post("/restaurant", payload).then((r) => r.data);
export const updateMyRestaurant = (payload) => apiClient.put("/restaurant/profile", payload).then((r) => r.data);
export const getMyRestaurant = () => apiClient.get("/restaurant/profile").then((r) => r.data);
export const setRestaurantAvailability = (open) =>
  apiClient.put(`/restaurant/availability?open=${open}`).then((r) => r.data);
