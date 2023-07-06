package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.services.UserServiceI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Mock
    private UserServiceI userServiceI;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp(){

    }
    @Test
    void createUser() {

          }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void getUserByEmail() {
    }

    @Test
    void getUserByKeyword() {
    }
}