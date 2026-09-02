package com.foodgo.admin.dto;

import java.math.BigDecimal;

public class DashboardStatsDto {
    private long totalUsers;
    private long totalCustomers;
    private long totalRestaurantOwners;
    private long totalRestaurants;
    private long pendingRestaurantApprovals;
    private long totalOrders;
    private long deliveredOrders;
    private long cancelledOrders;
    private BigDecimal totalRevenue;

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getTotalRestaurantOwners() {
        return totalRestaurantOwners;
    }

    public void setTotalRestaurantOwners(long totalRestaurantOwners) {
        this.totalRestaurantOwners = totalRestaurantOwners;
    }

    public long getTotalRestaurants() {
        return totalRestaurants;
    }

    public void setTotalRestaurants(long totalRestaurants) {
        this.totalRestaurants = totalRestaurants;
    }

    public long getPendingRestaurantApprovals() {
        return pendingRestaurantApprovals;
    }

    public void setPendingRestaurantApprovals(long pendingRestaurantApprovals) {
        this.pendingRestaurantApprovals = pendingRestaurantApprovals;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getDeliveredOrders() {
        return deliveredOrders;
    }

    public void setDeliveredOrders(long deliveredOrders) {
        this.deliveredOrders = deliveredOrders;
    }

    public long getCancelledOrders() {
        return cancelledOrders;
    }

    public void setCancelledOrders(long cancelledOrders) {
        this.cancelledOrders = cancelledOrders;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
