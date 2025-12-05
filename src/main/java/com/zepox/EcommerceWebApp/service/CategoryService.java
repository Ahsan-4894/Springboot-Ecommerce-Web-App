package com.zepox.EcommerceWebApp.service;


import com.zepox.EcommerceWebApp.dto.request.AddCategoryRequestDto;
import com.zepox.EcommerceWebApp.dto.response.AddCategoryResponseDto;
import com.zepox.EcommerceWebApp.dto.response.GetCategoriesResponseDto;
import com.zepox.EcommerceWebApp.dto.response.GetCategoryResponseDto;
import com.zepox.EcommerceWebApp.entity.Category;
import com.zepox.EcommerceWebApp.exception.custom.CategoryDoesNotExistException;
import com.zepox.EcommerceWebApp.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepo categoryRepo;

    @Cacheable(value = "CATEGORY", key = "#categoryName")
    public Category findCategoryByName(String categoryName){
        return categoryRepo.findByCategoryName(categoryName).orElseThrow(()-> new CategoryDoesNotExistException("Category doesn't exists"));
    }

    @Cacheable(value = "CATEGORY_ALL", key = "#populate")
    public GetCategoriesResponseDto getAllCategories(boolean populate) {
        List<Category> categories = categoryRepo.findAll();

        List<GetCategoryResponseDto> response;
        if(populate) {
            response = categories.stream()
                    .map(c-> GetCategoryResponseDto.builder()
                            .id(c.getId())
                            .ID(String.valueOf(c.getId()))
                            .name(c.getCategoryName())
                            .description(c.getCategoryDescription())
                            .build()
                    )
                    .toList();
        }else{
            response = categories.stream()
                    .map(c-> GetCategoryResponseDto.builder()
                            .name(c.getCategoryName())
                            .build()
                    )
                    .toList();
        }

        return GetCategoriesResponseDto.builder()
                .success(true)
                .message(response)
                .build();
    }

    public GetCategoriesResponseDto searchCategories() {
        return null;
    }

    public AddCategoryResponseDto addCategory(AddCategoryRequestDto category) {
        return null;
    }
}
