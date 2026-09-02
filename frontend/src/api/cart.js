import apiClient from "./client";

export const fetchCart = () => apiClient.get("/cart").then((r) => r.data);
export const addCartItem = (foodItemId, quantity = 1) =>
  apiClient.post("/cart/items", { foodItemId, quantity }).then((r) => r.data);
export const updateCartItem = (cartItemId, quantity) =>
  apiClient.put(`/cart/items/${cartItemId}`, { quantity }).then((r) => r.data);
export const removeCartItem = (cartItemId) => apiClient.delete(`/cart/items/${cartItemId}`).then((r) => r.data);
export const clearCart = () => apiClient.delete("/cart").then((r) => r.data);
