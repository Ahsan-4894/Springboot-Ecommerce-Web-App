package com.zepox.EcommerceWebApp.service;

import com.zepox.EcommerceWebApp.dto.request.AddProductRequestDto;
import com.zepox.EcommerceWebApp.dto.request.SearchProductsRequestDto;
import com.zepox.EcommerceWebApp.dto.request.UpdateProductRequestDto;
import com.zepox.EcommerceWebApp.dto.response.*;
import com.zepox.EcommerceWebApp.entity.Category;
import com.zepox.EcommerceWebApp.entity.Item;
import com.zepox.EcommerceWebApp.entity.User;
import com.zepox.EcommerceWebApp.exception.custom.ProductsDoesNotExistsException;
import com.zepox.EcommerceWebApp.exception.custom.UserDoesNotExistException;
import com.zepox.EcommerceWebApp.mapper.ProductMapper;
import com.zepox.EcommerceWebApp.repository.ProductRepo;
import com.zepox.EcommerceWebApp.util.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final ImageUploadService  imageUploadService;
    private final AuthContext authContext;
    private final UserService userService;
    private final CategoryService categoryService;

    @Cacheable(
            value = "PRODUCT_LIST",
            key = "#categoryNames"
    )
    public GetProductsResponseDto getProducts(List<String> categoryNames) {
        List<Item> products = productRepo.findByCategoryNames(categoryNames);
        if(products.isEmpty()) throw new ProductsDoesNotExistsException("Products not found");

        List<ProductsResponseDto> transformedProducts = productMapper.toDtos(products);
        return GetProductsResponseDto
                .builder()
                .success(true)
                .message(transformedProducts)
                .build();
    }

    @Cacheable(
            value = "PRODUCT",
            key = "#id",
            condition = "#id != null"
    )
    public GetProductsResponseDto getProductById(String id){
        Item item = productRepo.findById(id).orElse(null);
        if(item == null) throw new ProductsDoesNotExistsException("Product not found");
        ProductsResponseDto transformedProduct = productMapper.toDto(item);
        return GetProductsResponseDto.builder()
                .success(true)
                .message(List.of(transformedProduct))
                .build();
    }

    public GetPaginatedProductsResponseDto getPaginatedProducts(int page, String category, double priceLowerBound, double priceUpperBound)  {
        int productsPerPage = 4;
        Pageable pageable = PageRequest.of(page, productsPerPage);
        Page<Item> pagedItems;
        if(category!=null && !category.isEmpty()){
            pagedItems = productRepo.findByPriceRangeAndCategoryName(category, priceLowerBound, priceUpperBound, pageable);
        }else{
            pagedItems = productRepo.findByPriceRange(priceLowerBound, priceUpperBound, pageable);
        }

        if(pagedItems.isEmpty()) throw new ProductsDoesNotExistsException("Products not found");

        List<ProductsResponseDto> products = pagedItems.getContent().stream()
                .map(productMapper::toDto)
                .toList();
        return GetPaginatedProductsResponseDto.builder()
                .success(true)
                .message(products)
                .totalProducts(pagedItems.getTotalElements())
                .totalPages(pagedItems.getTotalPages())
                .build();
    }

    @Cacheable(
            value = "PRODUCT_SEARCH",
            key = "T(String).format('%s:%s:%s:%s:%s', " +
                    "#req.name() ?: 'null', " +
                    "#req.category() ?: 'null', " +
                    "#req.available() ?: 'null', " +
                    "#req.priceLowerBound(), " +
                    "#req.priceUpperBound())"
    )
    public SearchProductsResponseDto searchProducts(SearchProductsRequestDto req) {
//        If product ID is given, only search for that.
        if(req.id()!=null){
            GetProductsResponseDto product = this.getProductById(req.id());
            return SearchProductsResponseDto.builder()
                    .success(true)
                    .message(product.getMessage())
                    .build();
        }
        List<Item> products = productRepo.searchProducts(
                (req.name()!=null && !req.name().isBlank()) ? "%"+req.name()+"%" : null,
                (req.category()!=null && !req.category().isBlank()) ? req.category() : null,
                (req.available()!=null && !req.available().isBlank()) ? req.available() : null,
                req.priceLowerBound() > 0.0 ? req.priceLowerBound() : -1,
                req.priceUpperBound() > 0.0 ? req.priceUpperBound() : -1
        );
        if(products.isEmpty()) throw new ProductsDoesNotExistsException("Products not found");
        List<ProductsResponseDto> transformedProducts = productMapper.toDtos(products);
        return SearchProductsResponseDto.builder()
                .success(true)
                .message(transformedProducts)
                .build();
    }

    public GetProductsResponseDto getBestSoldProducts() {
//        Filtering out those items along with their sold_units that are placed in only those orders
//        Whom status is delivered

        int bestSoldProductsLimit = 5;
        List<Item> products = productRepo.findBestSellingProducts(bestSoldProductsLimit);
        if(products.isEmpty()) throw new ProductsDoesNotExistsException("Best selling products not found");
        List<ProductsResponseDto> transformedProducts = productMapper.toDtos(products);
        return GetProductsResponseDto.builder()
                .success(true)
                .message(transformedProducts)
                .build();
    }

    public GetProductsResponseDto getBestReviewedProducts() {
//        Considering best reviewed only those having avg(rating) of at least 3 stars
        List<Item> products = productRepo.findBestReviewedProducts();
        if(products.isEmpty()) throw new ProductsDoesNotExistsException("Best reviewed products not found");
        List<ProductsResponseDto> transformedProducts = productMapper.toDtos(products);
        return GetProductsResponseDto.builder()
                .success(true)
                .message(transformedProducts)
                .build();
    }

    public AddProductResponseDto addProduct(AddProductRequestDto dto) throws IOException {
        Category category = categoryService.findCategoryByName(dto.getCategoryName());
        ImageUploadServiceResponseDto imageUploadResult = imageUploadService.uploadImage(dto.getImage());
        String adminId = authContext.getIdOfCurrentLoggedInUser();

        User admin = userService.findUserById(adminId);

        Item newProduct =  Item.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .publicId(imageUploadResult.getPublicId())
                .imgUrl(imageUploadResult.getImageUrl())
                .description(dto.getDescription())
                .admin(admin)
                .category(category)
                .feedback(null)
                .orderItems(null)
                .build();

        productRepo.save(newProduct);
        return AddProductResponseDto.builder()
                .success(true)
                .message("Product successfully added")
                .build();
    }

    public UpdateProductResponseDto updateProduct(UpdateProductRequestDto dto) {
        Item product = productRepo.findById(dto.productId()).orElse(null);
        if(product==null) throw new ProductsDoesNotExistsException("Product not found");

        Category category = categoryService.findCategoryByName(dto.categoryName());

        String adminId = authContext.getIdOfCurrentLoggedInUser();

        User admin = userService.findUserById(adminId);

        product.setName(dto.name());
        product.setCategory(category);
        product.setPrice(dto.price());
        product.setQuantity(dto.quantity());
        product.setDescription(dto.description());
        product.setAdmin(admin);

        productRepo.save(product);
        return UpdateProductResponseDto.builder()
                .success(true)
                .message("Product successfully updated")
                .build();
    }

    @Transactional
    @Cacheable(
            value = "ALL_PRODUCTS",
            key = "#productIds"
    )
    public List<Item> getAllProductsByIds(List<String> productIds){
        return productRepo.findProductsByIds(productIds);
    }

//    @Cacheable(
//            value = "PRODUCT",
//            key = "#productId",
//            condition = "#productId != null"
//    )
    public Item findProductById(String productId){
        return productRepo.findById(productId).orElseThrow(()-> new ProductsDoesNotExistsException("Product not found"));
    }
    public void bulkUpdate(List<Item> items){
        productRepo.saveAll(items);
    }

}
