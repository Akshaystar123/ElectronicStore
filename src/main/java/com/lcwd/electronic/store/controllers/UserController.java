package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.payload.AppConstants;
import com.lcwd.electronic.store.services.FileServiceI;
import com.lcwd.electronic.store.services.UserServiceI;
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

 //   private Logger logger = LoggerFactory.getLogger(UserController.class);

    //create

    /**
     * @author Akshay Khaire
     * @param userDto
     * @return User
     * @apiNote Create User
     */
    //create
    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Initiating request to create User:{}",userDto.getName());
        UserDto user = userServiceI.createUser(userDto);
        log.info("Completed request of create User:{}",userDto.getName());
        return new ResponseEntity<UserDto>(user, HttpStatus.CREATED);
    }

    /**
     * @author Akshay Khaire
     * @param userDto
     * @return User
     * @apiNote Update User
     */
    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("userId") String userId, @Valid @RequestBody UserDto userDto) {
        log.info("Initiating request to update User:{}",userDto.getUserId());
        UserDto updatedUserDto = userServiceI.updateUser(userDto, userId);
        log.info("Completed request of update User:{}",userDto.getUserId());

        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    /**
     * @author Akshay Khaire
     * @param userId
     * @apiNote Delete User
     */
    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {
        log.info("Initiating request to delete User:{}",userId);
        userServiceI.deleteUser(userId);
        log.info("Completed request delete User:{}",userId);

        return new ResponseEntity<>(new ApiResponseMessage(AppConstants.DELETE_USER,true,HttpStatus.OK), HttpStatus.OK);
    }

    /**
     * @author Akshay Khaire
     * @return All User
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
     * @author Akshay Khaire
     * @param userId
     * @return UserDto
     * @apiNote Get User By Id
     */
    //get single user by id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        log.info("Initiating request to get User by Id:{}",userId);
        UserDto SingleUserById = userServiceI.getUserById(userId);
        log.info("Completed request of get User by Id:{}",userId);

        return new ResponseEntity<UserDto>(SingleUserById, HttpStatus.OK);
    }

    /**
     * @author Akshay Khaire
     * @param email
     * @return UserDto
     * @apiNote Get User By email
     */
    //get email by user
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        log.info("Initiating request to get User by email:{}",email);
        UserDto userByEmail = userServiceI.getUserByEmail(email);
        log.info("Completed request of get User by email:{}",email);

        return new ResponseEntity<>(userByEmail, HttpStatus.OK);
    }

    /**
     * @author Akshay Khaire
     * @param keywords
     * @return <List<UserDto>
     * @author Akshay Khaire
     * @apiNote Get User By Keyword
     */
    //search user
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> getUserByKeyword(@PathVariable String keywords) {
        log.info("Initiating request to search by keyword:{}",keywords);
        List<UserDto> userDtoByKeyword = userServiceI.searchUser(keywords);
        log.info("Completed request of search by keyword:{}",keywords);
        return new ResponseEntity<>(userDtoByKeyword, HttpStatus.OK);
    }

    /**
     * @author Akshay Khaire
     * @param image
     * @param userId
     * @return
     * @throws IOException
     * @apiNote upload user image
     */
    //upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image, @PathVariable String userId) throws IOException {
        log.info("Initiating request for upload user image:{}",userId);
        String imageName = fileServiceI.uploadFile(image, imageFullPath);
        UserDto user = userServiceI.getUserById(userId);
        user.setImageName(imageName);
        userServiceI.updateUser(user, userId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).multipartFile(imageName)
                .success(true).message(AppConstants.IMAGE_UPLOADED).status(HttpStatus.CREATED).build();
        log.info("Completed request for upload user image:{}",userId);
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    /**
     * @author Akshay Khaire
     * @param userId
     * @param response
     * @throws IOException
     * @apiNote get user upload image
     */
    //serve user image
    @GetMapping(value = "/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
       log.info("Initiating request for serve(get) user image:{}",userId);
        UserDto user = userServiceI.getUserById(userId);
        InputStream resource = fileServiceI.getResource(imageFullPath, user.getImageName());
        log.info("Completed request for serve(get) user image:{}",userId);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}


