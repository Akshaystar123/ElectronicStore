package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.services.CategoryServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
   private CategoryServiceI categoryServiceI;

    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto categoryDto1 = categoryServiceI.create(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/categoryId")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String categoryId, @RequestBody CategoryDto categoryDto) {
        CategoryDto updateCategory = categoryServiceI.update(categoryDto, categoryId);
        return new ResponseEntity<>(updateCategory, HttpStatus.OK);

    }
    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory (@PathVariable String categoryId){
        categoryServiceI.delete(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category is deleted successfully !!").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);

    }
    //gel all
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories (
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false)int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false)int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false)String sortDir
    ){
        PageableResponse<CategoryDto> pageableResponse = categoryServiceI.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }
    //get single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingleCategory (String categoryId){
        CategoryDto categoryDto2 = categoryServiceI.get(categoryId);
        return new ResponseEntity(categoryDto2, HttpStatus.OK);
    }
}
