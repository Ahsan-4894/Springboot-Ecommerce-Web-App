package com.zepox.EcommerceWebApp.controller.Public;

import com.zepox.EcommerceWebApp.dto.response.GetCategoriesResponseDto;
import com.zepox.EcommerceWebApp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/category")
@RequiredArgsConstructor
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping("/getCategories")
    public ResponseEntity<GetCategoriesResponseDto> getCategories(@RequestParam(defaultValue = "false") boolean populate) {
        return ResponseEntity.ok().body(categoryService.getAllCategories(populate));
    }

    @PostMapping("/searchCategories")
    public ResponseEntity<GetCategoriesResponseDto> searchCategories() {
        return ResponseEntity.ok().body(categoryService.searchCategories());
    }
}
