package com.foodgo.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndFoodItemId(Cart cart, Long foodItemId);
    Optional<CartItem> findByIdAndCart(Long id, Cart cart);
}
