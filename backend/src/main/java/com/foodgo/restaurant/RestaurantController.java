package com.foodgo.restaurant;

import com.foodgo.common.ApiResponse;
import com.foodgo.restaurant.dto.RestaurantDto;
import com.foodgo.restaurant.dto.RestaurantRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/api/restaurants")
    public ApiResponse<List<RestaurantDto>> getRestaurants(@RequestParam(required = false) String search,
                                                             @RequestParam(required = false) String cuisineType) {
        return ApiResponse.success("Restaurants fetched successfully",
                restaurantService.getApprovedRestaurants(search, cuisineType));
    }

    @GetMapping("/api/restaurants/{id}")
    public ApiResponse<RestaurantDto> getRestaurantById(@PathVariable Long id) {
        return ApiResponse.success("Restaurant fetched successfully", restaurantService.getRestaurantById(id));
    }

    @PostMapping("/api/restaurant")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_OWNER')")
    public ApiResponse<RestaurantDto> registerRestaurant(Authentication authentication,
                                                          @Valid @RequestBody RestaurantRequest request) {
        return ApiResponse.success("Restaurant registered successfully. Awaiting admin approval.",
                restaurantService.registerRestaurant(authentication.getName(), request));
    }

    @PutMapping("/api/restaurant/profile")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_OWNER')")
    public ApiResponse<RestaurantDto> updateMyRestaurant(Authentication authentication,
                                                          @Valid @RequestBody RestaurantRequest request) {
        return ApiResponse.success("Restaurant profile updated successfully",
                restaurantService.updateMyRestaurant(authentication.getName(), request));
    }

    @GetMapping("/api/restaurant/profile")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_OWNER')")
    public ApiResponse<RestaurantDto> getMyRestaurant(Authentication authentication) {
        return ApiResponse.success("Restaurant profile fetched successfully",
                restaurantService.getMyRestaurant(authentication.getName()));
    }

    @PutMapping("/api/restaurant/availability")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_OWNER')")
    public ApiResponse<Void> setAvailability(Authentication authentication, @RequestParam boolean open) {
        restaurantService.setAvailability(authentication.getName(), open);
        return ApiResponse.success("Availability updated successfully");
    }
}
