package com.zepox.EcommerceWebApp.repository;

import com.zepox.EcommerceWebApp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
    Optional<Category> findByCategoryName(String name);
}
