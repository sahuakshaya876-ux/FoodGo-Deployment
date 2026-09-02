package com.foodgo.review;

import com.foodgo.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRestaurantOrderByCreatedAtDesc(Restaurant restaurant);
    List<Review> findByRestaurant(Restaurant restaurant);
}
