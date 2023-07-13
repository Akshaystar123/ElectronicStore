package com.lcwd.electronic.store.services.Impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.UserRepositoryI;
import com.lcwd.electronic.store.services.UserServiceI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest(classes = UserServiceImplTest.class)
class UserServiceImplTest {
    @Mock
    private UserRepositoryI userRepositoryI;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    User user1;
    User user2;
    UserDto userDto;
    List<User> user;

    @BeforeEach
    void setUp() {

        String Id1 = UUID.randomUUID().toString();

        user1 = User.builder().userId(Id1)
                .name("Akki")
                .password("akki@123")
                .gender("Male")
                .email("akki@1234")
                .about("Java Developer")
                .imageName("aa.png")
                .build();

        String Id2 = UUID.randomUUID().toString();

        user2 = User.builder().userId(Id2)
                .name("Vishal")
                .password("vish@123")
                .gender("Male")
                .email("vish@1234")
                .about("Tester")
                .imageName("vv.png")
                .build();

        user = new ArrayList();

        user.add(user1);
        user.add(user2);

    }

    @Test
    void createUser() {
        Mockito.when(userRepositoryI.save(Mockito.any())).thenReturn(user1);

        UserDto user3 = userServiceImpl.createUser(modelMapper.map(user1, UserDto.class));

        assertEquals(user1.getName(), user3.getName());
    }

    @Test
    void updateUser() {

       /* Mockito.when(userRepositoryI.save(Mockito.any())).thenReturn(modelMapper.map(user1, UserDto.class));

        UserDto userDto1 = userServiceImpl.updateUser(modelMapper.map(user1, UserDto.class));

        assertEquals();
*/
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getAllUser() {
        int pageNumber = 0;
        int pageSize = 2;

        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        Mockito.when(userRepositoryI.findAll(pageable)).thenReturn((Page<User>) user);
        PageableResponse<UserDto> allUser = userServiceImpl.getAllUser(pageNumber,pageSize);
        int actualResult = allUser.size();
        assertEquals(2, actualResult);
    }

    @Test
    void getUserById() {
    }

    @Test
    void getUserByEmail() {
    }

    @Test
    void searchUser() {

        String keyword = "A";
        Mockito.when(userRepositoryI.findAll()).thenReturn(user);
        List<UserDto> userDtos = userServiceImpl.searchUser(keyword);
        assertNotNull(userDtos);
    }
}