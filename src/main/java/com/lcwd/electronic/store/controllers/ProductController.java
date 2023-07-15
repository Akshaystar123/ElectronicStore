package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.payload.AppConstants;
import com.lcwd.electronic.store.services.ProductServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductServiceI productServiceI;

    Logger logger= LoggerFactory.getLogger(ProductController.class);

    /**
     * @author Akshay Khaire
     * @param productDto
     * @return
     */
    @PostMapping()
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){

        logger.info("Initiating request to create Product");
        ProductDto product = productServiceI.createProduct(productDto);
        logger.info("Completed request of create Product");
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    /**
     * @author Akshay Khaire
     * @param productDto
     * @param productId
     * @return
     */
       @PutMapping("/{productId}")
       public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable String productId){
           logger.info("Initiating request to update Product");
           ProductDto productDto1 = productServiceI.updateProduct(productDto, productId);
           logger.info("Completed request of update Product");
           return new ResponseEntity<>(productDto1, HttpStatus.OK);
    }

    /**
     * @author Akshay Khaire
     * @param productId
     * @return
     */

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId){
        logger.info("Initiating request to delete Product");
        productServiceI.deleteProduct(productId);
        logger.info("Completed request of delete Product");

        ApiResponseMessage deletedProduct = ApiResponseMessage.builder().message(AppConstants.DELETE_PRODUCT + productId).status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(deletedProduct,HttpStatus.OK);
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
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct (
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false)int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false)int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false)String sortDir
    ){
        logger.info("Initiating request to get all Product");
        PageableResponse<ProductDto> pageableResponse = productServiceI.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Completed request of get all Product");
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    /**
     * @author Akshay Khaire
     * @param productId
     * @return
     */

    //get single
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingleCategory (@PathVariable String productId){
        logger.info("Initiating request to get Product");
        ProductDto singleProduct = productServiceI.getSingleProduct(productId);
        logger.info("Completed request of get Product");
        return new ResponseEntity(singleProduct, HttpStatus.OK);
    }

}
