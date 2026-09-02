import axios from "axios";

// The backend base URL is injected at build/runtime via Vite env vars so the
// same frontend image can point at different backends (local, staging, EKS)
// without a rebuild. Falls back to localhost:8080 for local dev.
const baseURL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8081/api";

const apiClient = axios.create({
  baseURL,
  headers: {
    "Content-Type": "application/json",
  },
});

apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem("foodgo_token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem("foodgo_token");
      localStorage.removeItem("foodgo_user");
    }
    return Promise.reject(error);
  }
);

export default apiClient;
