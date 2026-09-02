import apiClient from "./client";

export const fetchMyAddresses = () => apiClient.get("/addresses").then((r) => r.data);
export const addAddress = (payload) => apiClient.post("/addresses", payload).then((r) => r.data);
export const updateAddress = (id, payload) => apiClient.put(`/addresses/${id}`, payload).then((r) => r.data);
export const deleteAddress = (id) => apiClient.delete(`/addresses/${id}`).then((r) => r.data);
