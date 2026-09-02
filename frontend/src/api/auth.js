import apiClient from "./client";

export const registerUser = (payload) => apiClient.post("/auth/register", payload).then((r) => r.data);
export const loginUser = (payload) => apiClient.post("/auth/login", payload).then((r) => r.data);
export const getMyProfile = () => apiClient.get("/users/me").then((r) => r.data);
export const updateMyProfile = (payload) => apiClient.put("/users/me", payload).then((r) => r.data);
