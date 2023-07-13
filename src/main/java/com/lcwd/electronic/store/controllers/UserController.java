package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.payload.AppConstants;
import com.lcwd.electronic.store.services.FileServiceI;
import com.lcwd.electronic.store.services.UserServiceI;
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
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserServiceI userServiceI;

    @Autowired
    private FileServiceI fileServiceI;

    @Value("${user.profile.image.path}")
    private String imageFullPath;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    //create

    /**
     * @param userDto
     * @return User
     * @author Akshay Khaire
     * @apiNote Create User
     */
    //create
    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Initiating request to create User");
        UserDto user = userServiceI.createUser(userDto);
        log.info("Completed request of create User");
        return new ResponseEntity<UserDto>(user, HttpStatus.CREATED);
    }

    /**
     * @param userDto
     * @return User
     * @author Akshay Khaire
     * @apiNote Update User
     */
    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("userId") String userId, @Valid @RequestBody UserDto userDto) {
        log.info("Initiating request to update User");
        UserDto updatedUserDto = userServiceI.updateUser(userDto, userId);
        log.info("Completed request of update User");

        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    /**
     * @param userId
     * @author Akshay Khaire
     * @apiNote Delete User
     */
    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable String userId) {
        log.info("Initiating request to delete User");
        userServiceI.deleteUser(userId);
        log.info("Completed request delete User");

        return new ResponseEntity<>(AppConstants.DELETE_USER, HttpStatus.OK);
    }

    /**
     * @return All User
     * @author Akshay Khaire
     * @apiNote Get All User
     */
    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir

    ) {
        log.info("Initiating request to get all User");
        PageableResponse<UserDto> allUsers = (PageableResponse<UserDto>) userServiceI.getAllUser(pageNumber, pageSize, sortBy, sortDir);
        log.info("Completed request of get all User");

        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    /**
     * @param userId
     * @return UserDto
     * @author Akshay Khaire
     * @apiNote Get User By Id
     */
    //get single user by id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        log.info("Initiating request to get User by Id");
        UserDto SingleUserById = userServiceI.getUserById(userId);
        log.info("Completed request of get User by Id");

        return new ResponseEntity<UserDto>(SingleUserById, HttpStatus.OK);
    }

    /**
     * @param email
     * @return UserDto
     * @author Akshay Khaire
     * @apiNote Get User By email
     */
    //get email by user
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        log.info("Initiating request to get User by email");
        UserDto userByEmail = userServiceI.getUserByEmail(email);
        log.info("Completed request of get User by email");

        return new ResponseEntity<>(userByEmail, HttpStatus.OK);
    }

    /**
     * @param keywords
     * @return <List<UserDto>
     * @author Akshay Khaire
     * @apiNote Get User By Keyword
     */
    //search user
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> getUserByKeyword(@PathVariable String keywords) {
        log.info("Initiating request to search by keyword");
        List<UserDto> userDtoByKeyword = userServiceI.searchUser(keywords);
        log.info("Completed request of search by keyword");

        return new ResponseEntity<>(userDtoByKeyword, HttpStatus.OK);
    }

    //upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image, @PathVariable String userId) throws IOException {
        String imageName = fileServiceI.uploadFile(image, imageFullPath);
        UserDto user = userServiceI.getUserById(userId);
        user.setImageName(imageName);
        userServiceI.updateUser(user, userId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).multipartFile(imageName)
                .success(true).message("Image uploaded successfully").status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    //serve user image
    @GetMapping(value = "/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user = userServiceI.getUserById(userId);
        logger.info("User image name : {} ", user.getImageName());
        InputStream resource = fileServiceI.getResource(imageFullPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}


