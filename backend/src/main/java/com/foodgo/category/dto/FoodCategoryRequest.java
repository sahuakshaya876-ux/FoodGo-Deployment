package com.foodgo.category.dto;

import jakarta.validation.constraints.NotBlank;

public class FoodCategoryRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    private String imageUrl;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
