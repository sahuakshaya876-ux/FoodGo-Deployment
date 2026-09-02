package com.foodgo.order;

import com.foodgo.order.dto.OrderDto;
import com.foodgo.order.dto.PlaceOrderRequest;

import java.util.List;

public interface OrderService {
    OrderDto placeOrder(String customerEmail, PlaceOrderRequest request);
    List<OrderDto> getMyOrders(String customerEmail);
    OrderDto getMyOrderById(String customerEmail, Long orderId);
    OrderDto cancelOrder(String customerEmail, Long orderId);
    List<OrderDto> getRestaurantOrders(String ownerEmail);
    OrderDto updateOrderStatus(String ownerEmail, Long orderId, OrderStatus status);
    List<OrderDto> getAllOrders();
    OrderDto adminUpdateOrderStatus(Long orderId, OrderStatus status);
}
