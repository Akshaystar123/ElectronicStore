package com.lcwd.electronic.store.services.Impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.payload.AppConstants;
import com.lcwd.electronic.store.repositories.ProductRepositoryI;
import com.lcwd.electronic.store.services.ProductServiceI;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductServiceI {

    @Autowired
    private ProductRepositoryI productRepositoryI;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {

        Product product = modelMapper.map(productDto, Product.class);

        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        Product saveProduct = productRepositoryI.save(product);

        ProductDto productDto1 = modelMapper.map(saveProduct, ProductDto.class);

        return productDto1;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {

        Product product = productRepositoryI.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT_NOT_FOUND + productId));

        product.setTitle(productDto.getTitle());
        product.setQuantity(productDto.getQuantity());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setAddedDate(productDto.getAddedDate());
        product.setDiscountPrice(productDto.getDiscountPrice());

        Product updatedProduct = productRepositoryI.save(product);

        ProductDto updatedProductDto = modelMapper.map(updatedProduct, ProductDto.class);

        return updatedProductDto;
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepositoryI.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT_NOT_FOUND));
        productRepositoryI.delete(product);
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortBy.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageSize, pageNumber, sort);
        Page<Product> page = productRepositoryI.findAll(pageable);

        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);

        return pageableResponse;
    }

    @Override
    public ProductDto getSingleProduct(String productId) {

        Product singleProduct = productRepositoryI.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT_NOT_FOUND));
        ProductDto singleProductDto = modelMapper.map(singleProduct, ProductDto.class);
        return singleProductDto;
    }

    @Override
    public ProductDto searchProduct(String keyword) {

        List<ProductDto> ProductByTitleContaining = productRepositoryI.findByTitleContaining(keyword);
        ProductDto ProductDtoByTitleContaining = modelMapper.map(ProductByTitleContaining, ProductDto.class);
        return ProductDtoByTitleContaining;
    }
}
