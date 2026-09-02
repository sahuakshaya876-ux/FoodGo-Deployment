import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { CartProvider } from "./context/CartContext";
import Layout from "./components/Layout";
import ProtectedRoute from "./components/ProtectedRoute";

import LoginPage from "./pages/auth/LoginPage";
import RegisterPage from "./pages/auth/RegisterPage";

import HomePage from "./pages/customer/HomePage";
import RestaurantsPage from "./pages/customer/RestaurantsPage";
import RestaurantDetailPage from "./pages/customer/RestaurantDetailPage";
import CartPage from "./pages/customer/CartPage";
import CheckoutPage from "./pages/customer/CheckoutPage";
import OrdersPage from "./pages/customer/OrdersPage";
import OrderDetailPage from "./pages/customer/OrderDetailPage";
import WishlistPage from "./pages/customer/WishlistPage";
import ProfilePage from "./pages/customer/ProfilePage";

import RestaurantOwnerLayout from "./pages/restaurant/RestaurantOwnerLayout";
import RestaurantDashboardPage from "./pages/restaurant/RestaurantDashboardPage";
import RestaurantProfilePage from "./pages/restaurant/RestaurantProfilePage";
import RestaurantMenuPage from "./pages/restaurant/RestaurantMenuPage";
import RestaurantOrdersPage from "./pages/restaurant/RestaurantOrdersPage";
import RestaurantAnalyticsPage from "./pages/restaurant/RestaurantAnalyticsPage";

import AdminLayout from "./pages/admin/AdminLayout";
import AdminDashboardPage from "./pages/admin/AdminDashboardPage";
import AdminUsersPage from "./pages/admin/AdminUsersPage";
import AdminRestaurantsPage from "./pages/admin/AdminRestaurantsPage";
import AdminOrdersPage from "./pages/admin/AdminOrdersPage";
import AdminCategoriesPage from "./pages/admin/AdminCategoriesPage";

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <CartProvider>
          <Routes>
            <Route element={<Layout />}>
              {/* Public */}
              <Route path="/" element={<HomePage />} />
              <Route path="/restaurants" element={<RestaurantsPage />} />
              <Route path="/restaurants/:id" element={<RestaurantDetailPage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />

              {/* Customer */}
              <Route
                path="/cart"
                element={
                  <ProtectedRoute roles={["ROLE_CUSTOMER"]}>
                    <CartPage />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/checkout"
                element={
                  <ProtectedRoute roles={["ROLE_CUSTOMER"]}>
                    <CheckoutPage />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/orders"
                element={
                  <ProtectedRoute roles={["ROLE_CUSTOMER"]}>
                    <OrdersPage />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/orders/:id"
                element={
                  <ProtectedRoute roles={["ROLE_CUSTOMER"]}>
                    <OrderDetailPage />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/wishlist"
                element={
                  <ProtectedRoute roles={["ROLE_CUSTOMER"]}>
                    <WishlistPage />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/profile"
                element={
                  <ProtectedRoute>
                    <ProfilePage />
                  </ProtectedRoute>
                }
              />

              {/* Restaurant owner */}
              <Route
                path="/restaurant"
                element={
                  <ProtectedRoute roles={["ROLE_RESTAURANT_OWNER"]}>
                    <RestaurantOwnerLayout />
                  </ProtectedRoute>
                }
              >
                <Route path="dashboard" element={<RestaurantDashboardPage />} />
                <Route path="profile" element={<RestaurantProfilePage />} />
                <Route path="menu" element={<RestaurantMenuPage />} />
                <Route path="orders" element={<RestaurantOrdersPage />} />
                <Route path="analytics" element={<RestaurantAnalyticsPage />} />
              </Route>

              {/* Admin */}
              <Route
                path="/admin"
                element={
                  <ProtectedRoute roles={["ROLE_ADMIN"]}>
                    <AdminLayout />
                  </ProtectedRoute>
                }
              >
                <Route path="dashboard" element={<AdminDashboardPage />} />
                <Route path="users" element={<AdminUsersPage />} />
                <Route path="restaurants" element={<AdminRestaurantsPage />} />
                <Route path="orders" element={<AdminOrdersPage />} />
                <Route path="categories" element={<AdminCategoriesPage />} />
              </Route>

              {/* Fallback */}
              <Route path="*" element={<HomePage />} />
            </Route>
          </Routes>
        </CartProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}
