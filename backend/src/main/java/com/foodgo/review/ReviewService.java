package com.foodgo.review;

import com.foodgo.review.dto.ReviewDto;
import com.foodgo.review.dto.ReviewRequest;

import java.util.List;

public interface ReviewService {
    ReviewDto addReview(String customerEmail, ReviewRequest request);
    List<ReviewDto> getReviewsForRestaurant(Long restaurantId);
}
