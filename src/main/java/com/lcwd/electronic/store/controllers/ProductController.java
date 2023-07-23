package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.payload.AppConstants;
import com.lcwd.electronic.store.services.FileServiceI;
import com.lcwd.electronic.store.services.ProductServiceI;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {
    @Autowired
    private ProductServiceI productServiceI;
    @Autowired
    private FileServiceI fileServiceI;
    @Value("${product.profile.image.path}")
    private String imageFullPath;

    /**
     * @author Akshay Khaire
     * @param productDto
     * @return
     * @apiNote create product
     */
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){

        log.info("Initiating request to create Product{}",productDto.getTitle());
        ProductDto product = productServiceI.createProduct(productDto);
        log.info("Completed request of create Product{}",productDto.getTitle());
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    /**
     * @author Akshay Khaire
     * @param productDto
     * @param productId
     * @return
     * @apiNote update product
     */

       @PutMapping("/{productId}")
       public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable String productId){
           log.info("Initiating request to update Product{}",productId);
           ProductDto productDto1 = productServiceI.updateProduct(productDto, productId);
           log.info("Completed request of update Product{}",productId);
           return new ResponseEntity<>(productDto1, HttpStatus.OK);
    }

    /**
     * @author Akshay Khaire
     * @param productId
     * @return
     * @apiNote delete product
     */

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId){
        log.info("Initiating request to delete Product{}",productId);
        productServiceI.deleteProduct(productId);
        log.info("Completed request of delete Product{}",productId);
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
     * @apiNote get all product
     */

    //gel all
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct (
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false)int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "0", required = false)int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false)String sortDir
    ){
        log.info("Initiating request to get all Product");
        PageableResponse<ProductDto> pageableResponse = productServiceI.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        log.info("Completed request of get all Product");
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    /**
     * @author Akshay Khaire
     * @param productId
     * @return
     * @apiNote get single product
     */
    //get single
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingleCategory (@PathVariable String productId){
        log.info("Initiating request to get Product{}",productId);
        ProductDto singleProduct = productServiceI.getSingleProduct(productId);
        log.info("Completed request of get Product{}",productId);
        return new ResponseEntity(singleProduct, HttpStatus.OK);
    }

    /**
     * @author Akshay Khaire
     * @param image
     * @param productId
     * @return
     * @throws IOException
     * @apiNote upload product image
     */
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(@RequestParam("productImage") MultipartFile image, @PathVariable String productId) throws IOException {
        String imageName = fileServiceI.uploadFile(image, imageFullPath);
        log.info("Initiating request to upload Product image{}",productId);
        ProductDto singleProduct = productServiceI.getSingleProduct(productId);
        singleProduct.setProductImage(imageName);
        productServiceI.updateProduct(singleProduct,productId);
        log.info("Completed request to upload Product image{}",productId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).multipartFile(imageName)
                .success(true).message(AppConstants.IMAGE_UPLOADED).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    /**
     * @author Akshay Khaire
     * @param productId
     * @param response
     * @throws IOException
     * @apiNote get product image
     */
    //serve user image
    @GetMapping(value = "/image/{productId}")
    public void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto product = productServiceI.getSingleProduct(productId);
        log.info("Initiating the request to get uploaded product image{}",productId);
        InputStream resource = fileServiceI.getResource(imageFullPath, product.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
        log.info("Completed request to get uploaded Product image{}",productId);
    }
}
