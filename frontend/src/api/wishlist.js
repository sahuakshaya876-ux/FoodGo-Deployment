import apiClient from "./client";

export const fetchWishlist = () => apiClient.get("/wishlist").then((r) => r.data);
export const addToWishlist = (foodId) => apiClient.post(`/wishlist/${foodId}`).then((r) => r.data);
export const removeFromWishlist = (foodId) => apiClient.delete(`/wishlist/${foodId}`).then((r) => r.data);
