package com.foodgo.wishlist;

import com.foodgo.common.ApiResponse;
import com.foodgo.wishlist.dto.WishlistItemDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public ApiResponse<List<WishlistItemDto>> getMyWishlist(Authentication authentication) {
        return ApiResponse.success("Wishlist fetched successfully", wishlistService.getMyWishlist(authentication.getName()));
    }

    @PostMapping("/{foodId}")
    public ApiResponse<WishlistItemDto> addToWishlist(Authentication authentication, @PathVariable Long foodId) {
        return ApiResponse.success("Item added to wishlist", wishlistService.addToWishlist(authentication.getName(), foodId));
    }

    @DeleteMapping("/{foodId}")
    public ApiResponse<Void> removeFromWishlist(Authentication authentication, @PathVariable Long foodId) {
        wishlistService.removeFromWishlist(authentication.getName(), foodId);
        return ApiResponse.success("Item removed from wishlist");
    }
}
