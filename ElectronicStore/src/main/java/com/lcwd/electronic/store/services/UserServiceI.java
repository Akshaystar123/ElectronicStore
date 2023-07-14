package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.UserDto;

import java.util.List;

public interface UserServiceI {

    //create
   UserDto createUser(UserDto userDto);
    //update
   UserDto updateUser(UserDto userDto, String userId);
    //delete
   void deleteUser(String userId);
    //get all user
    List<UserDto> getAllUser(int pageNumber,int pageSize);
    //get single user by id
    UserDto getUserById(String userId);
    //get single user by email
    UserDto getUserByEmail(String email);
    //search user
    List<UserDto> searchUser(String keyword);

}