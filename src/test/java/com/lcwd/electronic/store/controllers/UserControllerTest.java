package com.lcwd.electronic.store.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.dtos.PageableResponse;
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

import java.util.Arrays;
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
    void getAllUsers() throws Exception {

        UserDto userDto1 = UserDto.builder().userId(UUID.randomUUID().toString())
                .gender("male")
                .name("Akki")
                .about("I am an java developer")
                .password("123")
                .email("akki123@gmail.com")
                .imageName("ak.png").build();

        UserDto userDto2 = UserDto.builder().userId(UUID.randomUUID().toString())
                .gender("male")
                .name("Sandip")
                .about("I am an tester")
                .password("456")
                .email("san123@gmail.com")
                .imageName("sn.png").build();

        UserDto userDto3 = UserDto.builder().userId(UUID.randomUUID().toString())
                .gender("male")
                .name("Rahul")
                .about("I am an java developer")
                .password("898")
                .email("rahul123@gmail.com")
                .imageName("rk.png").build();


        PageableResponse<UserDto> pageableResponse=new PageableResponse<>();

        pageableResponse.setContent(Arrays.asList(userDto1,userDto2,userDto3));
        pageableResponse.setPageNumber(0);
        pageableResponse.setPageSize(2);
        pageableResponse.setTotalElements(50);
        pageableResponse.setTotalPages(100);
        pageableResponse.setLastPage(false);

        Mockito.when(userServiceI.getAllUser(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);
    mockMvc.perform(MockMvcRequestBuilders.get("/users/")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

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