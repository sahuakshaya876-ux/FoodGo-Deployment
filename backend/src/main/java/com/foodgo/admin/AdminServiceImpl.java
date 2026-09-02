package com.foodgo.admin;

import com.foodgo.admin.dto.DashboardStatsDto;
import com.foodgo.exception.ResourceNotFoundException;
import com.foodgo.order.Order;
import com.foodgo.order.OrderMapper;
import com.foodgo.order.OrderRepository;
import com.foodgo.order.OrderStatus;
import com.foodgo.order.dto.OrderDto;
import com.foodgo.restaurant.Restaurant;
import com.foodgo.restaurant.RestaurantMapper;
import com.foodgo.restaurant.RestaurantRepository;
import com.foodgo.restaurant.RestaurantStatus;
import com.foodgo.restaurant.dto.RestaurantDto;
import com.foodgo.user.Role;
import com.foodgo.user.User;
import com.foodgo.user.UserMapper;
import com.foodgo.user.UserRepository;
import com.foodgo.user.dto.UserDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public AdminServiceImpl(UserRepository userRepository, UserMapper userMapper,
                             RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper,
                             OrderRepository orderRepository, OrderMapper orderMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalCustomers(userRepository.countByRole(Role.ROLE_CUSTOMER));
        stats.setTotalRestaurantOwners(userRepository.countByRole(Role.ROLE_RESTAURANT_OWNER));
        stats.setTotalRestaurants(restaurantRepository.count());
        stats.setPendingRestaurantApprovals(restaurantRepository.countByStatus(RestaurantStatus.PENDING_APPROVAL));
        stats.setTotalOrders(orderRepository.count());
        stats.setDeliveredOrders(orderRepository.countByStatus(OrderStatus.DELIVERED));
        stats.setCancelledOrders(orderRepository.countByStatus(OrderStatus.CANCELLED));

        BigDecimal revenue = orderRepository.sumRevenueFromDeliveredOrders();
        stats.setTotalRevenue(revenue != null ? revenue : BigDecimal.ZERO);

        return stats;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    @Override
    public UserDto setUserEnabled(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setEnabled(enabled);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantRepository.findAll().stream().map(restaurantMapper::toDto).toList();
    }

    @Override
    public RestaurantDto updateRestaurantStatus(Long restaurantId, RestaurantStatus status) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));
        restaurant.setStatus(status);
        return restaurantMapper.toDto(restaurantRepository.save(restaurant));
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(orderMapper::toDto).toList();
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setStatus(status);
        return orderMapper.toDto(orderRepository.save(order));
    }
}
