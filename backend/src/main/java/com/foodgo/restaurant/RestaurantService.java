package com.foodgo.restaurant;

import com.foodgo.restaurant.dto.RestaurantDto;
import com.foodgo.restaurant.dto.RestaurantRequest;

import java.util.List;

public interface RestaurantService {
    List<RestaurantDto> getApprovedRestaurants(String search, String cuisineType);
    RestaurantDto getRestaurantById(Long id);
    RestaurantDto registerRestaurant(String ownerEmail, RestaurantRequest request);
    RestaurantDto updateMyRestaurant(String ownerEmail, RestaurantRequest request);
    RestaurantDto getMyRestaurant(String ownerEmail);
    void setAvailability(String ownerEmail, boolean open);
    Restaurant getEntityById(Long id);
    Restaurant getEntityByOwnerEmail(String ownerEmail);
}
