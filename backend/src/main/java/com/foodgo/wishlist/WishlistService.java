package com.foodgo.wishlist;

import com.foodgo.wishlist.dto.WishlistItemDto;

import java.util.List;

public interface WishlistService {
    List<WishlistItemDto> getMyWishlist(String email);
    WishlistItemDto addToWishlist(String email, Long foodItemId);
    void removeFromWishlist(String email, Long foodItemId);
}
