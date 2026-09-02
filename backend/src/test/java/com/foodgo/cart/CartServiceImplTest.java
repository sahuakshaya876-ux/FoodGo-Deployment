package com.foodgo.cart;

import com.foodgo.cart.dto.AddCartItemRequest;
import com.foodgo.cart.dto.CartDto;
import com.foodgo.exception.BadRequestException;
import com.foodgo.food.FoodItem;
import com.foodgo.food.FoodItemService;
import com.foodgo.restaurant.Restaurant;
import com.foodgo.user.User;
import com.foodgo.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private UserService userService;

    @Mock
    private FoodItemService foodItemService;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Cart cart;
    private FoodItem foodItem;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("jane@example.com");

        cart = new Cart();
        cart.setId(100L);
        cart.setUser(user);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        foodItem = new FoodItem();
        foodItem.setId(7L);
        foodItem.setName("Veg Burger");
        foodItem.setPrice(BigDecimal.valueOf(150));
        foodItem.setAvailable(true);
        foodItem.setRestaurant(restaurant);
    }

    @Test
    void addItem_shouldThrowBadRequest_whenFoodItemUnavailable() {
        when(userService.getUserEntityByEmail("jane@example.com")).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        foodItem.setAvailable(false);
        AddCartItemRequest request = new AddCartItemRequest();
        request.setFoodItemId(7L);
        request.setQuantity(1);

        when(foodItemService.getEntityById(7L)).thenReturn(foodItem);

        assertThatThrownBy(() -> cartService.addItem("jane@example.com", request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("unavailable");
    }

    @Test
    void addItem_shouldAddNewCartItem_whenNotAlreadyInCart() {
        when(userService.getUserEntityByEmail("jane@example.com")).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(foodItemService.getEntityById(7L)).thenReturn(foodItem);
        when(cartItemRepository.findByCartAndFoodItemId(cart, 7L)).thenReturn(Optional.empty());
        when(cartRepository.findById(100L)).thenReturn(Optional.of(cart));

        CartDto dto = new CartDto();
        when(cartMapper.toDto(cart)).thenReturn(dto);

        AddCartItemRequest request = new AddCartItemRequest();
        request.setFoodItemId(7L);
        request.setQuantity(2);

        CartDto result = cartService.addItem("jane@example.com", request);

        assertThat(result).isNotNull();
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(2);
    }
}
