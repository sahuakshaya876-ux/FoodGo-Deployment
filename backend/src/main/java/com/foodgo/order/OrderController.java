package com.foodgo.order;

import com.foodgo.common.ApiResponse;
import com.foodgo.order.dto.OrderDto;
import com.foodgo.order.dto.PlaceOrderRequest;
import com.foodgo.order.dto.UpdateOrderStatusRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ApiResponse<OrderDto> placeOrder(Authentication authentication, @Valid @RequestBody PlaceOrderRequest request) {
        return ApiResponse.success("Order placed successfully", orderService.placeOrder(authentication.getName(), request));
    }

    @GetMapping("/api/orders")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ApiResponse<List<OrderDto>> getMyOrders(Authentication authentication) {
        return ApiResponse.success("Orders fetched successfully", orderService.getMyOrders(authentication.getName()));
    }

    @GetMapping("/api/orders/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ApiResponse<OrderDto> getMyOrderById(Authentication authentication, @PathVariable Long id) {
        return ApiResponse.success("Order fetched successfully", orderService.getMyOrderById(authentication.getName(), id));
    }

    @DeleteMapping("/api/orders/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ApiResponse<OrderDto> cancelOrder(Authentication authentication, @PathVariable Long id) {
        return ApiResponse.success("Order cancelled successfully", orderService.cancelOrder(authentication.getName(), id));
    }

    @GetMapping("/api/restaurant/orders")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_OWNER')")
    public ApiResponse<List<OrderDto>> getRestaurantOrders(Authentication authentication) {
        return ApiResponse.success("Orders fetched successfully", orderService.getRestaurantOrders(authentication.getName()));
    }

    @PutMapping("/api/restaurant/orders/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_RESTAURANT_OWNER')")
    public ApiResponse<OrderDto> updateOrderStatus(Authentication authentication, @PathVariable Long id,
                                                    @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ApiResponse.success("Order status updated successfully",
                orderService.updateOrderStatus(authentication.getName(), id, request.getStatus()));
    }
}
