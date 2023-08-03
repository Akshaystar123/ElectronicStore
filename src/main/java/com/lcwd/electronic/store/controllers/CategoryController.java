package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.payload.AppConstants;
import com.lcwd.electronic.store.services.CategoryServiceI;
import com.lcwd.electronic.store.services.FileServiceI;
import com.lcwd.electronic.store.services.ProductServiceI;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/categories")
@Slf4j
public class CategoryController {

    @Autowired
   private CategoryServiceI categoryServiceI;

    @Autowired
    private ProductServiceI productServiceI;

    @Autowired
    private FileServiceI fileServiceI;

    @Value("${category.profile.image.path}")
    private String imageFullPath;

    //private Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     ** @author Akshay Khaire
     * @param categoryDto
     * @return
     * @apiNote create new category
     */
    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Initiating request to create Category:{}",categoryDto.getTitle());
        CategoryDto categoryDto1 = categoryServiceI.create(categoryDto);
        log.info("Completed request of create Category:{}",categoryDto.getTitle());
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
    @PutMapping("/{categoryId}")

    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String categoryId, @RequestBody CategoryDto categoryDto) {
        log.info("Initiating request to update Category:{}",categoryId);
        CategoryDto updateCategory = categoryServiceI.update(categoryDto, categoryId);
        log.info("Completed request of update Category:{}",categoryId);
        return new ResponseEntity<>(updateCategory, HttpStatus.OK);

    }

    /**
     * @author Akshay Khaire
     * @param categoryId
     * @return
     * @apiNote delete category
     */

    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory (@PathVariable String categoryId){
        log.info("Initiating request to delete Category:{}",categoryId);
        categoryServiceI.delete(categoryId);
        log.info("Completed request of delete Category:{}",categoryId);
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
        log.info("Initiating request to get all Category");
        PageableResponse<CategoryDto> pageableResponse = categoryServiceI.getAll(pageNumber, pageSize, sortBy, sortDir);
        log.info("Completed request of grt all Category");
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    /**
     * @author Akshay Khaire
     * @param categoryId
     * @return
     * @apiNote get single category
     */
     //single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingleCategory (String categoryId){
        log.info("Initiating request to get single Category:{}",categoryId);
        CategoryDto categoryDto2 = categoryServiceI.get(categoryId);
        log.info("Completed request of get single Category:{}",categoryId);
        return new ResponseEntity(categoryDto2, HttpStatus.OK);
    }

    /**
     * @author Akshay Khaire
     * @param image
     * @param categoryId
     * @return
     * @throws IOException
     * @apiNote upload category image
     */
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(@RequestParam("categoryImage") MultipartFile image, @PathVariable String categoryId) throws IOException {
        String imageName = fileServiceI.uploadFile(image, imageFullPath);
        log.info("Initiating request to post Category image:{}",categoryId);
        CategoryDto category = categoryServiceI.get(categoryId);
        category.setCoverImage(imageName);
        categoryServiceI.update(category, categoryId);
        log.info("Completed request of post Category image:{}",categoryId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).multipartFile(imageName)
                .success(true).message(AppConstants.IMAGE_UPLOADED).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    /**
     * @author Akshay Khaire
     * @param categoryId
     * @param response
     * @throws IOException
     * @apiNote get upload category image
     */
    @GetMapping(value = "/image/{categoryId}")
    public void serveCategoryImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto category = categoryServiceI.get(categoryId);
        log.info("Initiating request to get Category image:{}",categoryId);
        InputStream resource = fileServiceI.getResource(imageFullPath, category.getCoverImage());
        log.info("Completed request of get Category image:{}",categoryId);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

    /**
     * @author Akshay Khaire
     * @param productDto
     * @param categoryId
     * @return
     * @apiNote create product with category
     */
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@RequestBody ProductDto productDto,@PathVariable String categoryId)
    {
        log.info("Initiating request to post product with category:{}",categoryId);
        ProductDto productWithCategory = productServiceI.createProductWithCategory(productDto, categoryId);
        log.info("Completed request of post product with category:{}",categoryId);
        return new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);
    }

}
