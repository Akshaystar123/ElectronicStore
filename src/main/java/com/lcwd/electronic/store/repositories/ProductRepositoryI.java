package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepositoryI extends JpaRepository<Product, String> {

    List<ProductDto> findByTitleContaining(String keyword);

}
