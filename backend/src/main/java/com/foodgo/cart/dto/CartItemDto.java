package com.foodgo.cart.dto;

import java.math.BigDecimal;

public class CartItemDto {
    private Long id;
    private Long foodItemId;
    private String foodItemName;
    private String foodItemImageUrl;
    private BigDecimal price;
    private int quantity;
    private BigDecimal lineTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public String getFoodItemName() {
        return foodItemName;
    }

    public void setFoodItemName(String foodItemName) {
        this.foodItemName = foodItemName;
    }

    public String getFoodItemImageUrl() {
        return foodItemImageUrl;
    }

    public void setFoodItemImageUrl(String foodItemImageUrl) {
        this.foodItemImageUrl = foodItemImageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }
}
