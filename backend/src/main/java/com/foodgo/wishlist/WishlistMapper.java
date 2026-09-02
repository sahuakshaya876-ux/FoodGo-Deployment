package com.foodgo.wishlist;

import com.foodgo.wishlist.dto.WishlistItemDto;
import org.springframework.stereotype.Component;

@Component
public class WishlistMapper {

    public WishlistItemDto toDto(Wishlist wishlist) {
        WishlistItemDto dto = new WishlistItemDto();
        dto.setId(wishlist.getId());
        dto.setFoodItemId(wishlist.getFoodItem().getId());
        dto.setFoodItemName(wishlist.getFoodItem().getName());
        dto.setFoodItemImageUrl(wishlist.getFoodItem().getImageUrl());
        dto.setPrice(wishlist.getFoodItem().getPrice());
        dto.setRestaurantId(wishlist.getFoodItem().getRestaurant().getId());
        dto.setRestaurantName(wishlist.getFoodItem().getRestaurant().getName());
        return dto;
    }
}
