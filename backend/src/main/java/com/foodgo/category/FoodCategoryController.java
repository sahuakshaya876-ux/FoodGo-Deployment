package com.foodgo.category;

import com.foodgo.category.dto.FoodCategoryDto;
import com.foodgo.category.dto.FoodCategoryRequest;
import com.foodgo.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class FoodCategoryController {

    private final FoodCategoryService categoryService;

    public FoodCategoryController(FoodCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ApiResponse<List<FoodCategoryDto>> getAllCategories() {
        return ApiResponse.success("Categories fetched successfully", categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ApiResponse<FoodCategoryDto> getCategoryById(@PathVariable Long id) {
        return ApiResponse.success("Category fetched successfully", categoryService.getCategoryById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RESTAURANT_OWNER')")
    public ApiResponse<FoodCategoryDto> createCategory(@Valid @RequestBody FoodCategoryRequest request) {
        return ApiResponse.success("Category created successfully", categoryService.createCategory(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RESTAURANT_OWNER')")
    public ApiResponse<FoodCategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody FoodCategoryRequest request) {
        return ApiResponse.success("Category updated successfully", categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.success("Category deleted successfully");
    }
}
