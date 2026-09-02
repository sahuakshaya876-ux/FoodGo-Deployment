package com.foodgo.food;

import com.foodgo.common.ApiResponse;
import com.foodgo.food.dto.FoodItemDto;
import com.foodgo.food.dto.FoodItemRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class FoodItemController {

    private final FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @GetMapping("/api/foods")
    public ApiResponse<List<FoodItemDto>> searchFoods(@RequestParam(required = false) String name,
                                                        @RequestParam(required = false) Boolean vegetarian,
                                                        @RequestParam(required = false) BigDecimal minPrice,
                                                        @RequestParam(required = false) BigDecimal maxPrice,
                                                        @RequestParam(required = false) Long categoryId) {
        return ApiResponse.success("Food items fetched successfully",
                foodItemService.searchFoodItems(name, vegetarian, minPrice, maxPrice, categoryId));
    }

    @GetMapping("/api/foods/{id}")
    public ApiResponse<FoodItemDto> getFoodById(@PathVariable Long id) {
        return ApiResponse.success("Food item fetched successfully", foodItemService.getFoodItemById(id));
    }

    @GetMapping("/api/restaurants/{restaurantId}/foods")
    public ApiResponse<List<FoodItemDto>> getFoodsByRestaurant(@PathVariable Long restaurantId) {
        return ApiResponse.success("Menu fetched successfully", foodItemService.getFoodItemsByRestaurant(restaurantId));
    }

    @PostMapping("/api/restaurant/menu")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_OWNER')")
    public ApiResponse<FoodItemDto> createFood(Authentication authentication, @Valid @RequestBody FoodItemRequest request) {
        return ApiResponse.success("Food item added successfully",
                foodItemService.createFoodItem(authentication.getName(), request));
    }

    @PutMapping("/api/restaurant/menu/{id}")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_OWNER')")
    public ApiResponse<FoodItemDto> updateFood(Authentication authentication, @PathVariable Long id,
                                                @Valid @RequestBody FoodItemRequest request) {
        return ApiResponse.success("Food item updated successfully",
                foodItemService.updateFoodItem(authentication.getName(), id, request));
    }

    @DeleteMapping("/api/restaurant/menu/{id}")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_OWNER')")
    public ApiResponse<Void> deleteFood(Authentication authentication, @PathVariable Long id) {
        foodItemService.deleteFoodItem(authentication.getName(), id);
        return ApiResponse.success("Food item deleted successfully");
    }

    @PutMapping("/api/restaurant/menu/{id}/availability")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_OWNER')")
    public ApiResponse<FoodItemDto> setAvailability(Authentication authentication, @PathVariable Long id,
                                                     @RequestParam boolean available) {
        return ApiResponse.success("Availability updated successfully",
                foodItemService.setAvailability(authentication.getName(), id, available));
    }
}
