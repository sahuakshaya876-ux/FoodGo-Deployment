package com.foodgo.order;

import com.foodgo.restaurant.Restaurant;
import com.foodgo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerOrderByCreatedAtDesc(User customer);
    Optional<Order> findByIdAndCustomer(Long id, User customer);
    List<Order> findByRestaurantOrderByCreatedAtDesc(Restaurant restaurant);
    Optional<Order> findByIdAndRestaurant(Long id, Restaurant restaurant);
    long countByRestaurant(Restaurant restaurant);
    long countByStatus(OrderStatus status);

    @Query("select coalesce(sum(o.totalAmount), 0) from Order o where o.status = com.foodgo.order.OrderStatus.DELIVERED")
    BigDecimal sumRevenueFromDeliveredOrders();
}
