package com.lcwd.electronic.store.services.Impl;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryrepositoryI;
import com.lcwd.electronic.store.services.CategoryServiceI;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryServiceI {

    @Autowired
    private CategoryrepositoryI categoryrepositoryI;
    @Autowired
    private ModelMapper mapper;
    @Override
    public CategoryDto create(CategoryDto categoryDto) {

        //creating categoryId:randomly
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryrepositoryI.save(category);
        return mapper.map(savedCategory, CategoryDto.class);
    }
    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        //get category
        Category category = categoryrepositoryI.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found exception"));
        //updates category details
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updatedCategory = categoryrepositoryI.save(category);
        return mapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryrepositoryI.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found exception"));
         categoryrepositoryI.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<Category> page = categoryrepositoryI.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page,CategoryDto.class);
        return pageableResponse;
    }

    @Override
    public CategoryDto get(String categoryId) {

        Category category = categoryrepositoryI.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found exception"));
        return mapper.map(category,CategoryDto.class);
    }
}
