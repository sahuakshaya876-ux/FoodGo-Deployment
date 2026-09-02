import apiClient from "./client";

export const submitReview = (payload) => apiClient.post("/reviews", payload).then((r) => r.data);
