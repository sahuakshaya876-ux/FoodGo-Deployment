package com.foodgo.category;

import com.foodgo.category.dto.FoodCategoryDto;
import com.foodgo.category.dto.FoodCategoryRequest;

import java.util.List;

public interface FoodCategoryService {
    List<FoodCategoryDto> getAllCategories();
    FoodCategoryDto getCategoryById(Long id);
    FoodCategoryDto createCategory(FoodCategoryRequest request);
    FoodCategoryDto updateCategory(Long id, FoodCategoryRequest request);
    void deleteCategory(Long id);
    FoodCategory getEntityById(Long id);
}
