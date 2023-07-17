package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;

import java.util.List;

public interface ProductServiceI {

    //create
    ProductDto createProduct(ProductDto productDto);

    //update
    ProductDto updateProduct(ProductDto productDto, String productId);
    //delete
    void deleteProduct(String productId);

    //getAll
    PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir);
    //get single product
    ProductDto getSingleProduct(String productId);
    //search product
    ProductDto searchProduct(String keyword);

    ProductDto createProductWithCategory(ProductDto productDto,String categoryId);
}
