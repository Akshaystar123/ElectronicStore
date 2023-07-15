package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.services.CategoryServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
   private CategoryServiceI categoryServiceI;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * @author Akshay Khaire
     * @param categoryDto
     * @return
     */

    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        logger.info("Initiating request to create Category");
        CategoryDto categoryDto1 = categoryServiceI.create(categoryDto);
        logger.info("Completed request of create Category");
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    /**
     * @author Akshay Khaire
     * @param categoryId
     * @param categoryDto
     * @return
     * @apiNote update category
     */

    //update
    @PutMapping("/categoryId")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String categoryId, @RequestBody CategoryDto categoryDto) {
        logger.info("Initiating request to update Category");
        CategoryDto updateCategory = categoryServiceI.update(categoryDto, categoryId);
        logger.info("Completed request of update Category");
        return new ResponseEntity<>(updateCategory, HttpStatus.OK);

    }

    /**
     * @author Akshay Khaire
     * @param categoryId
     * @return
     */

    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory (@PathVariable String categoryId){
        logger.info("Initiating request to delete Category");
        categoryServiceI.delete(categoryId);
        logger.info("Completed request of delete Category");
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category is deleted successfully !!").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);

    }

    /**
     * @author Akshay Khaire
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */

    //gel all
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories (
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false)int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false)int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false)String sortDir
    ){
        logger.info("Initiating request to get all Category");
        PageableResponse<CategoryDto> pageableResponse = categoryServiceI.getAll(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Completed request of grt all Category");
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    /**
     * @author Akshay Khaire
     * @param categoryId
     * @return
     */
     //single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingleCategory (String categoryId){
        logger.info("Initiating request to get single Category");
        CategoryDto categoryDto2 = categoryServiceI.get(categoryId);
        logger.info("Completed request of get single Category");
        return new ResponseEntity(categoryDto2, HttpStatus.OK);
    }
}
