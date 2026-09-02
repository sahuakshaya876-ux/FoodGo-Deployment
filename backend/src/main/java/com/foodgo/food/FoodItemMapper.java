package com.foodgo.food;

import com.foodgo.category.FoodCategory;
import com.foodgo.food.dto.FoodItemDto;
import org.springframework.stereotype.Component;

@Component
public class FoodItemMapper {

    public FoodItemDto toDto(FoodItem item) {
        FoodItemDto dto = new FoodItemDto();
        dto.setId(item.getId());
        dto.setRestaurantId(item.getRestaurant().getId());
        dto.setRestaurantName(item.getRestaurant().getName());

        FoodCategory category = item.getCategory();
        if (category != null) {
            dto.setCategoryId(category.getId());
            dto.setCategoryName(category.getName());
        }

        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setPrice(item.getPrice());
        dto.setImageUrl(item.getImageUrl());
        dto.setVegetarian(item.isVegetarian());
        dto.setAvailable(item.isAvailable());
        dto.setAverageRating(item.getAverageRating());
        dto.setTotalReviews(item.getTotalReviews());
        return dto;
    }
}
