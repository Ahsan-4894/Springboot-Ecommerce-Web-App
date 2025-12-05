package com.zepox.EcommerceWebApp.controller.Admin;

import com.zepox.EcommerceWebApp.dto.request.AddCategoryRequestDto;
import com.zepox.EcommerceWebApp.dto.response.AddCategoryResponseDto;
import com.zepox.EcommerceWebApp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;
    @PostMapping("/add")
    public ResponseEntity<AddCategoryResponseDto> addCategory(@RequestBody AddCategoryRequestDto category) {
        return ResponseEntity
                .ok()
                .body(categoryService.addCategory(category));
    }
}
