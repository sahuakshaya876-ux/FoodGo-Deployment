package com.foodgo.food;

import com.foodgo.category.FoodCategoryService;
import com.foodgo.exception.ResourceNotFoundException;
import com.foodgo.food.dto.FoodItemDto;
import com.foodgo.restaurant.Restaurant;
import com.foodgo.restaurant.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FoodItemServiceImplTest {

    @Mock
    private FoodItemRepository foodItemRepository;

    @Mock
    private FoodItemMapper foodItemMapper;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private FoodCategoryService foodCategoryService;

    @InjectMocks
    private FoodItemServiceImpl foodItemService;

    private FoodItem foodItem;

    @BeforeEach
    void setUp() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Spice Hub");

        foodItem = new FoodItem();
        foodItem.setId(5L);
        foodItem.setName("Paneer Tikka");
        foodItem.setPrice(BigDecimal.valueOf(199));
        foodItem.setRestaurant(restaurant);
        foodItem.setAvailable(true);
    }

    @Test
    void getFoodItemById_shouldReturnDto_whenFound() {
        when(foodItemRepository.findById(5L)).thenReturn(Optional.of(foodItem));

        FoodItemDto dto = new FoodItemDto();
        dto.setId(5L);
        dto.setName("Paneer Tikka");
        when(foodItemMapper.toDto(foodItem)).thenReturn(dto);

        FoodItemDto result = foodItemService.getFoodItemById(5L);

        assertThat(result.getName()).isEqualTo("Paneer Tikka");
    }

    @Test
    void getFoodItemById_shouldThrowNotFound_whenMissing() {
        when(foodItemRepository.findById(123L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> foodItemService.getFoodItemById(123L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void searchFoodItems_shouldReturnOnlyAvailableItems() {
        FoodItem unavailable = new FoodItem();
        unavailable.setId(6L);
        unavailable.setAvailable(false);
        unavailable.setRestaurant(foodItem.getRestaurant());

        when(foodItemRepository.findByNameContainingIgnoreCase("Paneer")).thenReturn(List.of(foodItem, unavailable));

        FoodItemDto dto = new FoodItemDto();
        dto.setId(5L);
        when(foodItemMapper.toDto(foodItem)).thenReturn(dto);

        List<FoodItemDto> result = foodItemService.searchFoodItems("Paneer", null, null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(5L);
    }
}
