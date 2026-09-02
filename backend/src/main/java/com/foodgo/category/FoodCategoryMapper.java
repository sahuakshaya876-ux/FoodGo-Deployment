package com.foodgo.category;

import com.foodgo.category.dto.FoodCategoryDto;
import org.springframework.stereotype.Component;

@Component
public class FoodCategoryMapper {

    public FoodCategoryDto toDto(FoodCategory category) {
        FoodCategoryDto dto = new FoodCategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setImageUrl(category.getImageUrl());
        dto.setDescription(category.getDescription());
        return dto;
    }
}
