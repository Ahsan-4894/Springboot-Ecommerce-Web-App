package com.zepox.EcommerceWebApp.controller.Admin;

import com.zepox.EcommerceWebApp.dto.request.AddProductRequestDto;
import com.zepox.EcommerceWebApp.dto.request.UpdateProductRequestDto;
import com.zepox.EcommerceWebApp.dto.response.AddProductResponseDto;
import com.zepox.EcommerceWebApp.dto.response.UpdateProductResponseDto;
import com.zepox.EcommerceWebApp.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class AdminProductController {
    private final ProductService productService;
    @PostMapping("/add")
    public ResponseEntity<AddProductResponseDto> addProduct(@ModelAttribute AddProductRequestDto dto) throws IOException {
        return ResponseEntity.ok().body(productService.addProduct(dto));
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateProductResponseDto> updateProduct(@Valid @RequestBody UpdateProductRequestDto dto){
        return ResponseEntity.ok().body(productService.updateProduct(dto));
    }



}
