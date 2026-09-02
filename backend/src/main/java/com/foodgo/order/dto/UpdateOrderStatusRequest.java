package com.foodgo.order.dto;

import com.foodgo.order.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateOrderStatusRequest {

    @NotNull(message = "Status is required")
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
