package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.payload.AppConstants;
import com.lcwd.electronic.store.services.UserServiceI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserServiceI userServiceI;

    /**
     * @author Akshay Khaire
     * @apiNote Create User
     * @param userDto
     * @return User
     */
    //create
    @PostMapping("/")
   public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        log.info("Initiating request to create User");
    UserDto user = userServiceI.createUser(userDto);
        log.info("Completed request of create User");
        return new ResponseEntity<>(user, HttpStatus.CREATED);
}
    /**
     * @author Akshay Khaire
     * @apiNote Update User
     * @param userDto
     * @return User
     */
    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("userId") String userId,@Valid @RequestBody UserDto userDto){
        log.info("Initiating request to update User");
        UserDto updatedUserDto = userServiceI.updateUser(userDto,userId);
        log.info("Completed request of update User");

        return  new ResponseEntity<>(updatedUserDto,HttpStatus.OK);
    }
    /**
     * @author Akshay Khaire
     * @apiNote Delete User
     * @param userId
     *
     */
    //delete
    @DeleteMapping("/{userId}")
    public  ResponseEntity deleteUser(@PathVariable String userId){
        log.info("Initiating request to delete User");
        userServiceI.deleteUser(userId);
        log.info("Completed request delete User");

        return new ResponseEntity<>(AppConstants.DELETE_USER,HttpStatus.OK);
    }
    /**
     * @author Akshay Khaire
     * @apiNote Get All User
     * @return  All User
     */
    //get all
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize
    ){
        log.info("Initiating request to get all User");
        List<UserDto> allUsers = userServiceI.getAllUser(pageNumber, pageSize);
        log.info("Completed request of get all User");

        return new ResponseEntity<List<UserDto>>(allUsers,HttpStatus.OK);
}
    /**
     * @author Akshay Khaire
     * @apiNote Get User By Id
     * @param userId
     * @return User
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
     * @author Akshay Khaire
     * @apiNote Get User By email
     * @param email
     * @return User
     */
    //get email by user
        @GetMapping("/email/{email}")
        public ResponseEntity<UserDto> getUserByEmail (@PathVariable String email) {
            log.info("Initiating request to get User by email");
            UserDto userByEmail = userServiceI.getUserByEmail(email);
            log.info("Completed request of get User by email");

            return new ResponseEntity<>(userByEmail, HttpStatus.OK);
        }
    /**
     * @author Akshay Khaire
     * @apiNote Get User By Keyword
     * @param keywords
     * @return User
     */
            //search user
            @GetMapping("/search/{keywords}")
            public ResponseEntity<List<UserDto>> getUserByKeyword (@PathVariable String keywords) {
                log.info("Initiating request to search by keyword");
                List<UserDto> userDtoByKeyword = userServiceI.searchUser(keywords);
                log.info("Completed request of search by keyword");

                return new ResponseEntity<>(userDtoByKeyword, HttpStatus.OK);

            }


}


