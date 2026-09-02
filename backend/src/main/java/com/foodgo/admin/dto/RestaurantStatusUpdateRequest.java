package com.foodgo.admin.dto;

import com.foodgo.restaurant.RestaurantStatus;
import jakarta.validation.constraints.NotNull;

public class RestaurantStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private RestaurantStatus status;

    public RestaurantStatus getStatus() {
        return status;
    }

    public void setStatus(RestaurantStatus status) {
        this.status = status;
    }
}
