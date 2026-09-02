package com.foodgo.cart;

import com.foodgo.cart.dto.AddCartItemRequest;
import com.foodgo.cart.dto.CartDto;
import com.foodgo.cart.dto.UpdateCartItemRequest;
import com.foodgo.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ApiResponse<CartDto> getCart(Authentication authentication) {
        return ApiResponse.success("Cart fetched successfully", cartService.getCart(authentication.getName()));
    }

    @PostMapping("/items")
    public ApiResponse<CartDto> addItem(Authentication authentication, @Valid @RequestBody AddCartItemRequest request) {
        return ApiResponse.success("Item added to cart", cartService.addItem(authentication.getName(), request));
    }

    @PutMapping("/items/{id}")
    public ApiResponse<CartDto> updateItem(Authentication authentication, @PathVariable Long id,
                                            @Valid @RequestBody UpdateCartItemRequest request) {
        return ApiResponse.success("Cart item updated successfully", cartService.updateItem(authentication.getName(), id, request));
    }

    @DeleteMapping("/items/{id}")
    public ApiResponse<CartDto> removeItem(Authentication authentication, @PathVariable Long id) {
        return ApiResponse.success("Item removed from cart", cartService.removeItem(authentication.getName(), id));
    }

    @DeleteMapping
    public ApiResponse<Void> clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
        return ApiResponse.success("Cart cleared successfully");
    }
}
