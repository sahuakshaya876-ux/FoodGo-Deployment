import { createContext, useContext, useEffect, useState } from "react";
import { loginUser, registerUser } from "../api/auth";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const storedUser = localStorage.getItem("foodgo_user");
    const storedToken = localStorage.getItem("foodgo_token");
    if (storedUser && storedToken) {
      setUser(JSON.parse(storedUser));
    }
    setLoading(false);
  }, []);

  const persistSession = (authResponse) => {
    localStorage.setItem("foodgo_token", authResponse.token);
    localStorage.setItem("foodgo_user", JSON.stringify(authResponse.user));
    setUser(authResponse.user);
  };

  const login = async (credentials) => {
    const response = await loginUser(credentials);
    persistSession(response.data);
    return response.data.user;
  };

  const register = async (payload) => {
    const response = await registerUser(payload);
    persistSession(response.data);
    return response.data.user;
  };

  const logout = () => {
    localStorage.removeItem("foodgo_token");
    localStorage.removeItem("foodgo_user");
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, setUser, login, register, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
