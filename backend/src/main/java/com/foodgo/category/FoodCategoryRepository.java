package com.foodgo.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {
    boolean existsByNameIgnoreCase(String name);
    Optional<FoodCategory> findByNameIgnoreCase(String name);
}
