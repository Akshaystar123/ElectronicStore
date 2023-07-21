package com.lcwd.electronic.store.services.Impl;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.payload.AppConstants;
import com.lcwd.electronic.store.repositories.CategoryrepositoryI;
import com.lcwd.electronic.store.services.CategoryServiceI;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryServiceI {

    @Autowired
    private CategoryrepositoryI categoryrepositoryI;
    @Autowired
    private ModelMapper mapper;

    @Value("${category.profile.image.path}")
    private String imagePath;

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Override
    public CategoryDto create(CategoryDto categoryDto) {

        //creating categoryId:randomly.
        log.info("Sending dao call to create category");
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryrepositoryI.save(category);
        log.info("Completed dao call to successful create category");
        return mapper.map(savedCategory, CategoryDto.class);
    }
    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        //get category
        log.info("Sending dao call to update category");
        Category category = categoryrepositoryI.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATEGORY_NOT_FOUND));
        //updates category details
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryrepositoryI.save(category);
        log.info("Completed dao call to successful update category");
        return mapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        log.info("Sending dao call to update category");
        Category category = categoryrepositoryI.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.DELETE_CATEGORY));
        String fullPath = imagePath + category.getCoverImage();
        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         categoryrepositoryI.delete(category);
        log.info("Completed dao call to successful delete category");
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
        log.info("Sending dao call to get all category");
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<Category> page = categoryrepositoryI.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page,CategoryDto.class);
        log.info("Completed dao call to successful get all category");
        return pageableResponse;
    }

    @Override
    public CategoryDto get(String categoryId) {
        log.info("Sending dao call to get category");
        Category category = categoryrepositoryI.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATEGORY_NOT_FOUND));
        log.info("Completed dao call to successful get category");
        return mapper.map(category,CategoryDto.class);
    }
}
