package com.foodgo.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddCartItemRequest {

    @NotNull(message = "Food item id is required")
    private Long foodItemId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity = 1;

    public Long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
