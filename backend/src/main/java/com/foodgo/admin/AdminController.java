package com.foodgo.admin;

import com.foodgo.admin.dto.DashboardStatsDto;
import com.foodgo.admin.dto.RestaurantStatusUpdateRequest;
import com.foodgo.admin.dto.UserStatusUpdateRequest;
import com.foodgo.common.ApiResponse;
import com.foodgo.order.dto.OrderDto;
import com.foodgo.order.dto.UpdateOrderStatusRequest;
import com.foodgo.restaurant.dto.RestaurantDto;
import com.foodgo.user.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ApiResponse<DashboardStatsDto> getDashboard() {
        return ApiResponse.success("Dashboard stats fetched successfully", adminService.getDashboardStats());
    }

    @GetMapping("/users")
    public ApiResponse<List<UserDto>> getAllUsers() {
        return ApiResponse.success("Users fetched successfully", adminService.getAllUsers());
    }

    @PutMapping("/users/{id}/status")
    public ApiResponse<UserDto> updateUserStatus(@PathVariable Long id, @Valid @RequestBody UserStatusUpdateRequest request) {
        return ApiResponse.success("User status updated successfully", adminService.setUserEnabled(id, request.getEnabled()));
    }

    @GetMapping("/restaurants")
    public ApiResponse<List<RestaurantDto>> getAllRestaurants() {
        return ApiResponse.success("Restaurants fetched successfully", adminService.getAllRestaurants());
    }

    @PutMapping("/restaurants/{id}/status")
    public ApiResponse<RestaurantDto> updateRestaurantStatus(@PathVariable Long id,
                                                              @Valid @RequestBody RestaurantStatusUpdateRequest request) {
        return ApiResponse.success("Restaurant status updated successfully",
                adminService.updateRestaurantStatus(id, request.getStatus()));
    }

    @GetMapping("/orders")
    public ApiResponse<List<OrderDto>> getAllOrders() {
        return ApiResponse.success("Orders fetched successfully", adminService.getAllOrders());
    }

    @PutMapping("/orders/{id}/status")
    public ApiResponse<OrderDto> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ApiResponse.success("Order status updated successfully", adminService.updateOrderStatus(id, request.getStatus()));
    }
}
