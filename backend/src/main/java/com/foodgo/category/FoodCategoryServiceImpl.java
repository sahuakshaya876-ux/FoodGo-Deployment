package com.foodgo.category;

import com.foodgo.category.dto.FoodCategoryDto;
import com.foodgo.category.dto.FoodCategoryRequest;
import com.foodgo.exception.ConflictException;
import com.foodgo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodCategoryServiceImpl implements FoodCategoryService {

    private final FoodCategoryRepository categoryRepository;
    private final FoodCategoryMapper categoryMapper;

    public FoodCategoryServiceImpl(FoodCategoryRepository categoryRepository, FoodCategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<FoodCategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toDto).toList();
    }

    @Override
    public FoodCategoryDto getCategoryById(Long id) {
        return categoryMapper.toDto(getEntityById(id));
    }

    @Override
    public FoodCategoryDto createCategory(FoodCategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ConflictException("A category with this name already exists");
        }
        FoodCategory category = new FoodCategory();
        category.setName(request.getName());
        category.setImageUrl(request.getImageUrl());
        category.setDescription(request.getDescription());
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public FoodCategoryDto updateCategory(Long id, FoodCategoryRequest request) {
        FoodCategory category = getEntityById(id);
        category.setName(request.getName());
        category.setImageUrl(request.getImageUrl());
        category.setDescription(request.getDescription());
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        FoodCategory category = getEntityById(id);
        categoryRepository.delete(category);
    }

    @Override
    public FoodCategory getEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}
