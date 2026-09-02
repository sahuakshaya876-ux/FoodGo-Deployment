package com.foodgo.review;

import com.foodgo.food.FoodItem;
import com.foodgo.food.FoodItemService;
import com.foodgo.restaurant.Restaurant;
import com.foodgo.restaurant.RestaurantService;
import com.foodgo.review.dto.ReviewDto;
import com.foodgo.review.dto.ReviewRequest;
import com.foodgo.user.User;
import com.foodgo.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final FoodItemService foodItemService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewMapper reviewMapper, UserService userService,
                              RestaurantService restaurantService, FoodItemService foodItemService) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.foodItemService = foodItemService;
    }

    @Override
    @Transactional
    public ReviewDto addReview(String customerEmail, ReviewRequest request) {
        User customer = userService.getUserEntityByEmail(customerEmail);
        Restaurant restaurant = restaurantService.getEntityById(request.getRestaurantId());

        Review review = new Review();
        review.setCustomer(customer);
        review.setRestaurant(restaurant);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        if (request.getFoodItemId() != null) {
            FoodItem foodItem = foodItemService.getEntityById(request.getFoodItemId());
            review.setFoodItem(foodItem);
            recalculateFoodItemRating(foodItem, request.getRating());
        }

        Review saved = reviewRepository.save(review);
        recalculateRestaurantRating(restaurant, request.getRating());

        return reviewMapper.toDto(saved);
    }

    @Override
    public List<ReviewDto> getReviewsForRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantService.getEntityById(restaurantId);
        return reviewRepository.findByRestaurantOrderByCreatedAtDesc(restaurant).stream().map(reviewMapper::toDto).toList();
    }

    private void recalculateRestaurantRating(Restaurant restaurant, int newRating) {
        int totalReviews = restaurant.getTotalReviews();
        BigDecimal currentTotal = restaurant.getAverageRating().multiply(BigDecimal.valueOf(totalReviews));
        int updatedCount = totalReviews + 1;
        BigDecimal updatedAverage = currentTotal.add(BigDecimal.valueOf(newRating))
                .divide(BigDecimal.valueOf(updatedCount), 2, RoundingMode.HALF_UP);

        restaurant.setAverageRating(updatedAverage);
        restaurant.setTotalReviews(updatedCount);
    }

    private void recalculateFoodItemRating(FoodItem foodItem, int newRating) {
        int totalReviews = foodItem.getTotalReviews();
        BigDecimal currentTotal = foodItem.getAverageRating().multiply(BigDecimal.valueOf(totalReviews));
        int updatedCount = totalReviews + 1;
        BigDecimal updatedAverage = currentTotal.add(BigDecimal.valueOf(newRating))
                .divide(BigDecimal.valueOf(updatedCount), 2, RoundingMode.HALF_UP);

        foodItem.setAverageRating(updatedAverage);
        foodItem.setTotalReviews(updatedCount);
    }
}
