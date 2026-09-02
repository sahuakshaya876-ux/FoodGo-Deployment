package com.foodgo.food;

import com.foodgo.category.FoodCategory;
import com.foodgo.category.FoodCategoryService;
import com.foodgo.exception.ResourceNotFoundException;
import com.foodgo.exception.UnauthorizedException;
import com.foodgo.food.dto.FoodItemDto;
import com.foodgo.food.dto.FoodItemRequest;
import com.foodgo.restaurant.Restaurant;
import com.foodgo.restaurant.RestaurantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class FoodItemServiceImpl implements FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private final FoodItemMapper foodItemMapper;
    private final RestaurantService restaurantService;
    private final FoodCategoryService foodCategoryService;

    public FoodItemServiceImpl(FoodItemRepository foodItemRepository, FoodItemMapper foodItemMapper,
                                RestaurantService restaurantService, FoodCategoryService foodCategoryService) {
        this.foodItemRepository = foodItemRepository;
        this.foodItemMapper = foodItemMapper;
        this.restaurantService = restaurantService;
        this.foodCategoryService = foodCategoryService;
    }

    @Override
    public List<FoodItemDto> searchFoodItems(String name, Boolean vegetarian, BigDecimal minPrice, BigDecimal maxPrice, Long categoryId) {
        List<FoodItem> items;
        if (StringUtils.hasText(name)) {
            items = foodItemRepository.findByNameContainingIgnoreCase(name);
        } else if (categoryId != null) {
            items = foodItemRepository.findByCategoryId(categoryId);
        } else if (vegetarian != null) {
            items = foodItemRepository.findByVegetarian(vegetarian);
        } else if (minPrice != null && maxPrice != null) {
            items = foodItemRepository.findByPriceBetween(minPrice, maxPrice);
        } else {
            items = foodItemRepository.findAll();
        }

        return items.stream()
                .filter(FoodItem::isAvailable)
                .map(foodItemMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodItemDto> getFoodItemsByRestaurant(Long restaurantId) {
        return foodItemRepository.findByRestaurantId(restaurantId).stream().map(foodItemMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FoodItemDto getFoodItemById(Long id) {
        return foodItemMapper.toDto(getEntityById(id));
    }

    @Override
    public FoodItemDto createFoodItem(String ownerEmail, FoodItemRequest request) {
        Restaurant restaurant = restaurantService.getEntityByOwnerEmail(ownerEmail);
        FoodItem item = new FoodItem();
        item.setRestaurant(restaurant);
        applyRequest(item, request);
        return foodItemMapper.toDto(foodItemRepository.save(item));
    }

    @Override
    public FoodItemDto updateFoodItem(String ownerEmail, Long foodItemId, FoodItemRequest request) {
        FoodItem item = getOwnedFoodItem(ownerEmail, foodItemId);
        applyRequest(item, request);
        return foodItemMapper.toDto(foodItemRepository.save(item));
    }

    @Override
    public void deleteFoodItem(String ownerEmail, Long foodItemId) {
        FoodItem item = getOwnedFoodItem(ownerEmail, foodItemId);
        foodItemRepository.delete(item);
    }

    @Override
    public FoodItemDto setAvailability(String ownerEmail, Long foodItemId, boolean available) {
        FoodItem item = getOwnedFoodItem(ownerEmail, foodItemId);
        item.setAvailable(available);
        return foodItemMapper.toDto(foodItemRepository.save(item));
    }

    @Override
    public FoodItem getEntityById(Long id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + id));
    }

    private FoodItem getOwnedFoodItem(String ownerEmail, Long foodItemId) {
        Restaurant restaurant = restaurantService.getEntityByOwnerEmail(ownerEmail);
        FoodItem item = getEntityById(foodItemId);
        if (!Objects.equals(item.getRestaurant().getId(), restaurant.getId())) {
            throw new UnauthorizedException("This food item does not belong to your restaurant");
        }
        return item;
    }

    private void applyRequest(FoodItem item, FoodItemRequest request) {
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setImageUrl(request.getImageUrl());
        item.setVegetarian(request.isVegetarian());
        item.setAvailable(request.isAvailable());
        if (request.getCategoryId() != null) {
            FoodCategory category = foodCategoryService.getEntityById(request.getCategoryId());
            item.setCategory(category);
        }
    }
}
