package com.foodgo.restaurant;

import com.foodgo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByOwner(User owner);

    boolean existsByOwner(User owner);

    List<Restaurant> findByNameContainingIgnoreCaseAndStatus(String name, RestaurantStatus status);

    List<Restaurant> findByStatus(RestaurantStatus status);

    List<Restaurant> findByCuisineTypeIgnoreCaseAndStatus(String cuisineType, RestaurantStatus status);

    long countByStatus(RestaurantStatus status);
}
