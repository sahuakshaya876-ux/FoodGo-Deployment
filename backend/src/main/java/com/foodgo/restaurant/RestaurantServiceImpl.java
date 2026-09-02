package com.foodgo.restaurant;

import com.foodgo.exception.ConflictException;
import com.foodgo.exception.ResourceNotFoundException;
import com.foodgo.restaurant.dto.RestaurantDto;
import com.foodgo.restaurant.dto.RestaurantRequest;
import com.foodgo.user.User;
import com.foodgo.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final UserService userService;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper,
                                  UserService userService) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
        this.userService = userService;
    }

    @Override
    public List<RestaurantDto> getApprovedRestaurants(String search, String cuisineType) {
        List<Restaurant> restaurants;
        if (StringUtils.hasText(cuisineType)) {
            restaurants = restaurantRepository.findByCuisineTypeIgnoreCaseAndStatus(cuisineType, RestaurantStatus.APPROVED);
        } else if (StringUtils.hasText(search)) {
            restaurants = restaurantRepository.findByNameContainingIgnoreCaseAndStatus(search, RestaurantStatus.APPROVED);
        } else {
            restaurants = restaurantRepository.findByStatus(RestaurantStatus.APPROVED);
        }
        return restaurants.stream().map(restaurantMapper::toDto).toList();
    }

    @Override
    public RestaurantDto getRestaurantById(Long id) {
        return restaurantMapper.toDto(getEntityById(id));
    }

    @Override
    public RestaurantDto registerRestaurant(String ownerEmail, RestaurantRequest request) {
        User owner = userService.getUserEntityByEmail(ownerEmail);

        if (restaurantRepository.existsByOwner(owner)) {
            throw new ConflictException("This account already has a registered restaurant");
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);
        applyRequest(restaurant, request);
        restaurant.setStatus(RestaurantStatus.PENDING_APPROVAL);

        return restaurantMapper.toDto(restaurantRepository.save(restaurant));
    }

    @Override
    public RestaurantDto updateMyRestaurant(String ownerEmail, RestaurantRequest request) {
        Restaurant restaurant = getEntityByOwnerEmail(ownerEmail);
        applyRequest(restaurant, request);
        return restaurantMapper.toDto(restaurantRepository.save(restaurant));
    }

    @Override
    public RestaurantDto getMyRestaurant(String ownerEmail) {
        return restaurantMapper.toDto(getEntityByOwnerEmail(ownerEmail));
    }

    @Override
    public void setAvailability(String ownerEmail, boolean open) {
        Restaurant restaurant = getEntityByOwnerEmail(ownerEmail);
        restaurant.setOpen(open);
        restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant getEntityById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
    }

    @Override
    public Restaurant getEntityByOwnerEmail(String ownerEmail) {
        User owner = userService.getUserEntityByEmail(ownerEmail);
        return restaurantRepository.findByOwner(owner)
                .orElseThrow(() -> new ResourceNotFoundException("No restaurant registered for this account"));
    }

    private void applyRequest(Restaurant restaurant, RestaurantRequest request) {
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setCuisineType(request.getCuisineType());
        restaurant.setImageUrl(request.getImageUrl());
        restaurant.setAddress(request.getAddress());
        restaurant.setCity(request.getCity());
        restaurant.setLatitude(request.getLatitude());
        restaurant.setLongitude(request.getLongitude());
        if (request.getDeliveryFee() != null) {
            restaurant.setDeliveryFee(request.getDeliveryFee());
        } else if (restaurant.getDeliveryFee() == null) {
            restaurant.setDeliveryFee(BigDecimal.valueOf(40));
        }
        if (request.getEstimatedDeliveryMinutes() != null) {
            restaurant.setEstimatedDeliveryMinutes(request.getEstimatedDeliveryMinutes());
        }
    }
}
