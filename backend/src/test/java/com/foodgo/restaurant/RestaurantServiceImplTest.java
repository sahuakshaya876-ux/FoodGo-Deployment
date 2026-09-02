package com.foodgo.restaurant;

import com.foodgo.exception.ResourceNotFoundException;
import com.foodgo.restaurant.dto.RestaurantDto;
import com.foodgo.user.User;
import com.foodgo.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setName("Spice Hub");
        restaurant.setStatus(RestaurantStatus.APPROVED);
    }

    @Test
    void getApprovedRestaurants_shouldReturnOnlyApprovedRestaurants() {
        when(restaurantRepository.findByStatus(RestaurantStatus.APPROVED)).thenReturn(List.of(restaurant));

        RestaurantDto dto = new RestaurantDto();
        dto.setId(10L);
        dto.setName("Spice Hub");
        when(restaurantMapper.toDto(restaurant)).thenReturn(dto);

        List<RestaurantDto> result = restaurantService.getApprovedRestaurants(null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Spice Hub");
    }

    @Test
    void getRestaurantById_shouldThrowNotFound_whenMissing() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantService.getRestaurantById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getRestaurantById_shouldReturnRestaurant_whenFound() {
        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));

        RestaurantDto dto = new RestaurantDto();
        dto.setId(10L);
        when(restaurantMapper.toDto(restaurant)).thenReturn(dto);

        RestaurantDto result = restaurantService.getRestaurantById(10L);

        assertThat(result.getId()).isEqualTo(10L);
    }
}
