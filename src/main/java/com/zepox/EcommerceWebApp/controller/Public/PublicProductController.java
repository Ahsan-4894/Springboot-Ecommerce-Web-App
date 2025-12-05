package com.zepox.EcommerceWebApp.controller.Public;

import com.zepox.EcommerceWebApp.dto.request.SearchProductsRequestDto;
import com.zepox.EcommerceWebApp.dto.response.GetPaginatedProductsResponseDto;
import com.zepox.EcommerceWebApp.dto.response.GetProductsResponseDto;
import com.zepox.EcommerceWebApp.dto.response.SearchProductsResponseDto;
import com.zepox.EcommerceWebApp.service.ProductService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/public/product")
public class PublicProductController {
    private final ProductService productService;
    @GetMapping("/getProducts")
    public ResponseEntity<GetProductsResponseDto> getProducts(
            @RequestParam(required = false) String categories
    ) {
        List<String> categoryNames = Arrays.asList(categories.split(","));
        return ResponseEntity.ok().body(productService.getProducts(categoryNames));
    }

    @GetMapping("/getAProduct/{id}")
    public ResponseEntity<GetProductsResponseDto> getAProduct(
            @PathVariable @NotBlank(message = "Product ID is missing.") String id
    ){
        return ResponseEntity.ok().body(productService.getProductById(id));
    }

    @GetMapping("/getPaginatedProducts")
    public ResponseEntity<GetPaginatedProductsResponseDto> getPaginatedProducts(
            @RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "0") String priceLowerBound,
            @RequestParam(defaultValue = "100") String priceUpperBound
    ){
        return ResponseEntity.ok().body(productService.getPaginatedProducts(
                Integer.parseInt(page), category, Double.parseDouble(priceLowerBound), Double.parseDouble(priceUpperBound)
        ));
    }

    @PostMapping("/searchProducts")
    public ResponseEntity<SearchProductsResponseDto> searchProducts(@RequestBody SearchProductsRequestDto dto){
        return ResponseEntity.ok().body(productService.searchProducts(dto));
    }

    @GetMapping("/getBestSoldProducts")
    public ResponseEntity<GetProductsResponseDto> getBestSoldProducts(){
        return ResponseEntity.ok().body(productService.getBestSoldProducts());
    }

    @GetMapping("/getBestReviewedProducts")
    public ResponseEntity<GetProductsResponseDto> getBestReviewedProducts(){
        return ResponseEntity.ok().body(productService.getBestReviewedProducts());
    }



}
