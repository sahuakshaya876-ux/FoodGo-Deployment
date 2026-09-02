package com.foodgo.order;

import com.foodgo.cart.Cart;
import com.foodgo.cart.CartItem;
import com.foodgo.cart.CartService;
import com.foodgo.exception.BadRequestException;
import com.foodgo.food.FoodItem;
import com.foodgo.order.dto.OrderDto;
import com.foodgo.order.dto.PlaceOrderRequest;
import com.foodgo.payment.Payment;
import com.foodgo.payment.PaymentMethod;
import com.foodgo.payment.PaymentService;
import com.foodgo.restaurant.Restaurant;
import com.foodgo.restaurant.RestaurantService;
import com.foodgo.user.User;
import com.foodgo.user.UserService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User customer;
    private Restaurant restaurant;
    private Cart cart;

    @BeforeEach
    void setUp() {
        customer = new User();
        customer.setId(1L);
        customer.setEmail("customer@example.com");

        restaurant = new Restaurant();
        restaurant.setId(2L);
        restaurant.setName("Tasty Bites");
        restaurant.setDeliveryFee(BigDecimal.valueOf(40));

        FoodItem foodItem = new FoodItem();
        foodItem.setId(3L);
        foodItem.setName("Chicken Roll");
        foodItem.setPrice(BigDecimal.valueOf(100));
        foodItem.setAvailable(true);
        foodItem.setRestaurant(restaurant);

        CartItem cartItem = new CartItem();
        cartItem.setFoodItem(foodItem);
        cartItem.setQuantity(2);

        cart = new Cart();
        cart.setUser(customer);
        cart.getItems().add(cartItem);
    }

    @Test
    void placeOrder_shouldThrowBadRequest_whenCartIsEmpty() {
        when(userService.getUserEntityByEmail("customer@example.com")).thenReturn(customer);
        when(cartService.getEntityByEmail("customer@example.com")).thenReturn(new Cart());

        PlaceOrderRequest request = new PlaceOrderRequest();
        request.setDeliveryAddressLine("221B Baker Street");
        request.setPaymentMethod(PaymentMethod.CASH_ON_DELIVERY);

        assertThatThrownBy(() -> orderService.placeOrder("customer@example.com", request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("empty");
    }

    @Test
    void placeOrder_shouldCalculateTotalsAndClearCart_whenCartHasItems() {
        when(userService.getUserEntityByEmail("customer@example.com")).thenReturn(customer);
        when(cartService.getEntityByEmail("customer@example.com")).thenReturn(cart);

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(500L);
            return order;
        });

        when(paymentService.createPaymentForOrder(any(Order.class), any(PaymentMethod.class), any(BigDecimal.class)))
                .thenReturn(mock(Payment.class));

        OrderDto expectedDto = new OrderDto();
        expectedDto.setId(500L);
        when(orderMapper.toDto(any(Order.class))).thenReturn(expectedDto);

        PlaceOrderRequest request = new PlaceOrderRequest();
        request.setDeliveryAddressLine("221B Baker Street");
        request.setPaymentMethod(PaymentMethod.CASH_ON_DELIVERY);

        OrderDto result = orderService.placeOrder("customer@example.com", request);

        assertThat(result.getId()).isEqualTo(500L);
        verify(cartService).clearCart("customer@example.com");
    }

    @Test
    void cancelOrder_shouldThrowBadRequest_whenOrderAlreadyDelivered() {
        Order order = new Order();
        order.setId(10L);
        order.setStatus(OrderStatus.DELIVERED);

        when(userService.getUserEntityByEmail("customer@example.com")).thenReturn(customer);
        when(orderRepository.findByIdAndCustomer(10L, customer)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.cancelOrder("customer@example.com", 10L))
                .isInstanceOf(BadRequestException.class);
    }
}
