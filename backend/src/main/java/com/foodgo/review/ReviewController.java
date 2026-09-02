package com.foodgo.review;

import com.foodgo.common.ApiResponse;
import com.foodgo.review.dto.ReviewDto;
import com.foodgo.review.dto.ReviewRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/api/reviews")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ApiResponse<ReviewDto> addReview(Authentication authentication, @Valid @RequestBody ReviewRequest request) {
        return ApiResponse.success("Review submitted successfully", reviewService.addReview(authentication.getName(), request));
    }

    @GetMapping("/api/restaurants/{id}/reviews")
    public ApiResponse<List<ReviewDto>> getReviews(@PathVariable Long id) {
        return ApiResponse.success("Reviews fetched successfully", reviewService.getReviewsForRestaurant(id));
    }
}
