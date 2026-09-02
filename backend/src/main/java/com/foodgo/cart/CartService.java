package com.foodgo.cart;

import com.foodgo.cart.dto.AddCartItemRequest;
import com.foodgo.cart.dto.CartDto;
import com.foodgo.cart.dto.UpdateCartItemRequest;

public interface CartService {
    CartDto getCart(String email);
    CartDto addItem(String email, AddCartItemRequest request);
    CartDto updateItem(String email, Long cartItemId, UpdateCartItemRequest request);
    CartDto removeItem(String email, Long cartItemId);
    void clearCart(String email);
    Cart getEntityByEmail(String email);
}
