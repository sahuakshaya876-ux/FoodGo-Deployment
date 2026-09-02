import apiClient from "./client";

export const fetchCategories = () => apiClient.get("/categories").then((r) => r.data);
export const createCategory = (payload) => apiClient.post("/categories", payload).then((r) => r.data);
export const updateCategory = (id, payload) => apiClient.put(`/categories/${id}`, payload).then((r) => r.data);
export const deleteCategory = (id) => apiClient.delete(`/categories/${id}`).then((r) => r.data);
