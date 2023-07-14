package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryrepositoryI extends JpaRepository<Category, String> {



}
