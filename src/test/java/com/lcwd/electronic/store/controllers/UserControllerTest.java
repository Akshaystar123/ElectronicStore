package com.lcwd.electronic.store.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.services.UserServiceI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @InjectMocks
    private UserController userController;
    @MockBean
    private UserServiceI userServiceI;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    private User user;

    @BeforeEach
    public void setup() {
        String id = UUID.randomUUID().toString();

         user = User.builder()
                .userId(id)
                .name("Akshay")
                .email("akki123@gmail.com")
                .password("12345")
                .gender("Male")
                .about("I am Developer")
                .imageName("aa.png")
                .build();

    }

    @Test
    void createUser() throws Exception{

        UserDto userDto = modelMapper.map(user, UserDto.class);

        Mockito.when(userServiceI.createUser(Mockito.any())).thenReturn(userDto);

        //actual request for url
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(user))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
              // .andExpect((ResultMatcher) jsonPath("$.name").exists());

    }

    private  String convertObjectToJsonString(Object user){

        try {
return new ObjectMapper().writeValueAsString(user);
        }catch (Exception e){
            e.printStackTrace();
return null;
        }
    }

    @Test
    void updateUser() throws  Exception{

        String userId = UUID.randomUUID().toString();
        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userServiceI.updateUser(userDto, userId)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/"+userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void deleteUser() throws  Exception{
        String userId = UUID.randomUUID().toString();
        UserDto userDto = modelMapper.map(user, UserDto.class);


        mockMvc.perform(MockMvcRequestBuilders.delete("/users/"+userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

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