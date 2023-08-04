package com.lcwd.electronic.store.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.services.CategoryServiceI;
import com.lcwd.electronic.store.services.ProductServiceI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @MockBean
    private CategoryServiceI categoryServiceI;

    @MockBean
    private ProductServiceI productServiceI;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    private Category category;

    @BeforeEach
    public void setUp() {
        String categoryId1 = UUID.randomUUID().toString();
        category = Category.builder()
                .categoryId(categoryId1)
                .title("laptops")
                .description("Smart Laptops is best")
                .coverImage("ask.png")
                .build();
    }
    @Test
    void createCategory() throws Exception{

        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

        Mockito.when(categoryServiceI.create(Mockito.any())).thenReturn(categoryDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/categories/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) jsonPath("$.title").exists());
    }
    private String convertObjectToJsonString(Object user) {

        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Test
    void updateCategory() throws Exception{

        String categoryId=UUID.randomUUID().toString();
        CategoryDto categoryDto=CategoryDto.builder()
                .categoryId(categoryId)
                .title("Mobiles")
                .description("Mobiles available with discounts")
                .coverImage("abc.png")
                .build();

        Mockito.when(categoryServiceI.update(Mockito.any(),Mockito.anyString())).thenReturn(categoryDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/categories/" +categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.categoryDescription").exists());

    }

    @Test
    void deleteCategory() throws Exception{

        String categoryId=UUID.randomUUID().toString();

        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/"+categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void getAllCategories() throws Exception{

        CategoryDto categoryDto1=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Mobiles")
                .description("Mobiles available for an playing games")
                .coverImage("ass.png")
                .build();
        CategoryDto categoryDto2=CategoryDto.builder().categoryId(UUID.randomUUID().toString())
                .title("Laptops")
                .description("Laptops available with good quality")
                .coverImage("ask.png")
                .build();

        PageableResponse<CategoryDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(categoryDto1,categoryDto2));
        pageableResponse.setPageNumber(0);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalPages(100);
        pageableResponse.setTotalElements(1000);
        pageableResponse.setLastPage(false);

        Mockito.when(categoryServiceI.getAll(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getSingleCategory() throws Exception{

        String categoryId=UUID.randomUUID().toString();

        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

        Mockito.when(categoryServiceI.get(categoryId)).thenReturn(categoryDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/"+categoryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.categoryDescription").exists());
    }

    @Test
    void uploadCategoryImage() {
    }

    @Test
    void serveCategoryImage() {
    }
    @Test
    void createProductWithCategory() throws Exception{

        String categoryId=UUID.randomUUID().toString();

        String productId= UUID.randomUUID().toString();
        Product product1 = Product.builder()
                .productId(productId)
                .title("Apple phone")
                .discountPrice(3000)
                .price(20000)
                .quantity(100)
                .live(true)
                .addedDate(new Date())
                .stock(true)
                .description("This mobile  has many fetures")
                .productImage("xyz.png")
                .category(category)
                .build();

        ProductDto productDto = modelMapper.map(product1, ProductDto.class);

        Mockito.when(productServiceI.createProductWithCategory(Mockito.any(),Mockito.anyString())).thenReturn(productDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/categories/"+categoryId+"/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(product1))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());
    }
}