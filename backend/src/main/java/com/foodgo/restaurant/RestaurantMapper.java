package com.foodgo.restaurant;

import com.foodgo.restaurant.dto.RestaurantDto;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    public RestaurantDto toDto(Restaurant restaurant) {
        RestaurantDto dto = new RestaurantDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setDescription(restaurant.getDescription());
        dto.setCuisineType(restaurant.getCuisineType());
        dto.setImageUrl(restaurant.getImageUrl());
        dto.setAddress(restaurant.getAddress());
        dto.setCity(restaurant.getCity());
        dto.setLatitude(restaurant.getLatitude());
        dto.setLongitude(restaurant.getLongitude());
        dto.setAverageRating(restaurant.getAverageRating());
        dto.setTotalReviews(restaurant.getTotalReviews());
        dto.setDeliveryFee(restaurant.getDeliveryFee());
        dto.setEstimatedDeliveryMinutes(restaurant.getEstimatedDeliveryMinutes());
        dto.setStatus(restaurant.getStatus());
        dto.setOpen(restaurant.isOpen());
        return dto;
    }
}
