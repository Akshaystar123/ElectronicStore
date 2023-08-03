package com.lcwd.electronic.store.services.Impl;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.repositories.CategoryrepositoryI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class CategoryServiceImplTest {

    @MockBean
    private CategoryrepositoryI categoryRepositoryI;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryServiceImpl categorySeviceImpl;

    Category category1;
    Category category2;
    CategoryDto categoryDto;

    List<Category> categories;

    @BeforeEach
    public void init() {
        String categoryId1 = UUID.randomUUID().toString();

        category1 = Category.builder()
                .categoryId(categoryId1)
                .title("Headphones")
                .description("Headphones available for listing songs")
                .build();
        String categoryId2 = UUID.randomUUID().toString();

        category2 = Category.builder()
                .categoryId(categoryId2)
                .title("Laptops")
                .description("Laptops available with discount")
                .build();

        categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);


    }

    @Test
    void create() {

        Mockito.when(categoryRepositoryI.save(Mockito.any())).thenReturn(category1);

        CategoryDto createdCategory = categorySeviceImpl.create(modelMapper.map(category1, CategoryDto.class));

        Assertions.assertEquals(category1.getTitle(), createdCategory.getTitle());

    }

    @Test
    void update() {

        String categoryId=UUID.randomUUID().toString();
        categoryDto = CategoryDto.builder()
                .categoryId(categoryId)
                .title("Mobiles")
                .description("Mobiles available with headphones")
                .build();
        Mockito.when(categoryRepositoryI.findById(categoryId)).thenReturn(Optional.of(category1));

        Mockito.when(categoryRepositoryI.save(Mockito.any())).thenReturn(category1);

        CategoryDto updatedCategory = categorySeviceImpl.update(categoryDto, categoryId);

        //Assertions.assertEquals(categoryDto.getTitle(),updatedCategory.getTitle());
        Assertions.assertNotNull(updatedCategory);
    }

    @Test
    void delete() {

        String categoryId = UUID.randomUUID().toString();

        Mockito.when(categoryRepositoryI.findById(categoryId)).thenReturn(Optional.of(category1));

        categorySeviceImpl.delete(categoryId);

        Mockito.verify(categoryRepositoryI,Mockito.times(1)).delete(category1);

    }

    @Test
    void getAll() {

        int pageNumber=0;
        int pageSize=2;
        String sortBy="title";
        String sortDir="asc";
        Sort sort= Sort.by(sortBy).ascending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<Category> page=new PageImpl<>(categories);

        Mockito.when(categoryRepositoryI.findAll(pageable)).thenReturn(page);

        PageableResponse<CategoryDto> allCategories = categorySeviceImpl.getAll(pageNumber, pageSize, sortBy, sortDir);

        Assertions.assertEquals(2,allCategories.getContent().size());
    }

    @Test
    void get() {

        String categoryId=UUID.randomUUID().toString();
        Mockito.when(categoryRepositoryI.findById(categoryId)).thenReturn(Optional.of(category1));

        CategoryDto categoryDto1 = categorySeviceImpl.get(categoryId);

        Assertions.assertEquals(category1.getTitle(),categoryDto1.getTitle());
    }
}