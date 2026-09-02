package com.foodgo.food;

import com.foodgo.food.dto.FoodItemDto;
import com.foodgo.food.dto.FoodItemRequest;

import java.math.BigDecimal;
import java.util.List;

public interface FoodItemService {
    List<FoodItemDto> searchFoodItems(String name, Boolean vegetarian, BigDecimal minPrice, BigDecimal maxPrice, Long categoryId);
    List<FoodItemDto> getFoodItemsByRestaurant(Long restaurantId);
    FoodItemDto getFoodItemById(Long id);
    FoodItemDto createFoodItem(String ownerEmail, FoodItemRequest request);
    FoodItemDto updateFoodItem(String ownerEmail, Long foodItemId, FoodItemRequest request);
    void deleteFoodItem(String ownerEmail, Long foodItemId);
    FoodItemDto setAvailability(String ownerEmail, Long foodItemId, boolean available);
    FoodItem getEntityById(Long id);
}
