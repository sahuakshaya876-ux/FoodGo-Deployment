package com.foodgo.review;

import com.foodgo.review.dto.ReviewDto;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewDto toDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setRestaurantId(review.getRestaurant().getId());
        dto.setFoodItemId(review.getFoodItem() != null ? review.getFoodItem().getId() : null);
        dto.setCustomerName(review.getCustomer().getFullName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}
