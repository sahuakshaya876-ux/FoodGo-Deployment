package com.foodgo.admin;

import com.foodgo.admin.dto.DashboardStatsDto;
import com.foodgo.order.dto.OrderDto;
import com.foodgo.restaurant.RestaurantStatus;
import com.foodgo.restaurant.dto.RestaurantDto;
import com.foodgo.user.dto.UserDto;

import java.util.List;

public interface AdminService {
    DashboardStatsDto getDashboardStats();
    List<UserDto> getAllUsers();
    UserDto setUserEnabled(Long userId, boolean enabled);
    List<RestaurantDto> getAllRestaurants();
    RestaurantDto updateRestaurantStatus(Long restaurantId, RestaurantStatus status);
    List<OrderDto> getAllOrders();
    OrderDto updateOrderStatus(Long orderId, com.foodgo.order.OrderStatus status);
}
