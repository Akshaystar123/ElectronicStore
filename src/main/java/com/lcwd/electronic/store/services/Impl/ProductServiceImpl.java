package com.lcwd.electronic.store.services.Impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.payload.AppConstants;
import com.lcwd.electronic.store.repositories.CategoryrepositoryI;
import com.lcwd.electronic.store.repositories.ProductRepositoryI;
import com.lcwd.electronic.store.services.ProductServiceI;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductServiceI {

    @Autowired
    private CategoryrepositoryI categoryrepositoryI;

    @Autowired
    private ProductRepositoryI productRepositoryI;
    @Autowired
    private ModelMapper modelMapper;
    @Value("${category.profile.image.path}")
    private String imageFullPath;

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
        product.setProductImage(productDto.getProductImage());
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
        String fullPath = imageFullPath + product.getProductImage();
        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    @Override
    public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {
        Category category = categoryrepositoryI.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATEGORY_NOT_FOUND + categoryId));
        Product product = modelMapper.map(productDto, Product.class);
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product savedProduct = productRepositoryI.save(product);
        ProductDto savedProductDto = modelMapper.map(savedProduct, ProductDto.class);
        return savedProductDto;
    }
}
