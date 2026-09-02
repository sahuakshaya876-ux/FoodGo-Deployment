package com.foodgo.food;

import com.foodgo.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    List<FoodItem> findByRestaurant(Restaurant restaurant);

    List<FoodItem> findByRestaurantId(Long restaurantId);

    List<FoodItem> findByNameContainingIgnoreCase(String name);

    List<FoodItem> findByVegetarian(boolean vegetarian);

    List<FoodItem> findByPriceBetween(BigDecimal min, BigDecimal max);

    List<FoodItem> findByCategoryId(Long categoryId);
}
