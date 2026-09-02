import { createContext, useCallback, useContext, useEffect, useState } from "react";
import { fetchCart, addCartItem, updateCartItem, removeCartItem, clearCart as clearCartApi } from "../api/cart";
import { useAuth } from "./AuthContext";

const CartContext = createContext(null);

export function CartProvider({ children }) {
  const { user } = useAuth();
  const [cart, setCart] = useState({ items: [], subtotal: 0 });
  const [loading, setLoading] = useState(false);

  const refreshCart = useCallback(async () => {
    if (!user || user.role !== "ROLE_CUSTOMER") {
      setCart({ items: [], subtotal: 0 });
      return;
    }
    setLoading(true);
    try {
      const response = await fetchCart();
      setCart(response.data);
    } finally {
      setLoading(false);
    }
  }, [user]);

  useEffect(() => {
    refreshCart();
  }, [refreshCart]);

  const addItem = async (foodItemId, quantity = 1) => {
    const response = await addCartItem(foodItemId, quantity);
    setCart(response.data);
  };

  const updateItem = async (cartItemId, quantity) => {
    const response = await updateCartItem(cartItemId, quantity);
    setCart(response.data);
  };

  const removeItem = async (cartItemId) => {
    const response = await removeCartItem(cartItemId);
    setCart(response.data);
  };

  const emptyCart = async () => {
    await clearCartApi();
    setCart({ items: [], subtotal: 0 });
  };

  const itemCount = cart.items?.reduce((sum, item) => sum + item.quantity, 0) || 0;

  return (
    <CartContext.Provider
      value={{ cart, loading, itemCount, refreshCart, addItem, updateItem, removeItem, emptyCart }}
    >
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error("useCart must be used within a CartProvider");
  }
  return context;
}
