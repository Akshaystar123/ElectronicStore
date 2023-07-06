package com.lcwd.electronic.store.services.Impl;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.payload.AppConstants;
import com.lcwd.electronic.store.repositories.UserRepositoryI;
import com.lcwd.electronic.store.services.UserServiceI;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserServiceI {
    @Autowired
    private UserRepositoryI userRepositoryI;
    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDto createUser(UserDto userDto) {

        //generate userId in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        //dto to entity
        User user = dtoToEntity(userDto);
        User saveUser = userRepositoryI.save(user);
        //dto to entity
        UserDto newDto = entityToDto(saveUser);
        return newDto;
    }

    private UserDto entityToDto(User saveUser) {


          /* UserDto userDto=UserDto.builder()
                .userId(saveUser.getUserId())
                .name(saveUser.getName())
                .email(saveUser.getEmail())
                .password(saveUser.getEmail())
                .about(saveUser.getAbout())
                .gender(saveUser.getGender())
                .imageName(saveUser.getImageName()).build();
*/

        return mapper.map(saveUser, UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {

        /*User user= User.builder()
                .userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password((userDto.getPassword()))
                .about(userDto.getAbout())
                .gender(userDto.getGender())
                .imageName(userDto.getImageName()).build();
*/
        return mapper.map(userDto, User.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        User user = userRepositoryI.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND));

        user.setName(userDto.getName());
        //email update
        user.setEmail(userDto.getEmail());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());

        User updatedUser = userRepositoryI.save(user);
        UserDto updatedDto = entityToDto(updatedUser);

        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepositoryI.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND));
        //delete user
        userRepositoryI.delete(user);
    }
    @Override
    public List<UserDto> getAllUser(int pageNumber,int pageSize) {

        PageRequest pageable = PageRequest.of(pageNumber, pageSize);

        Page<User> page = userRepositoryI.findAll(pageable);

        List<User> users = page.getContent();

        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

        return dtoList;

    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepositoryI.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepositoryI.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Enter valid email id"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {

        List<User> users = userRepositoryI.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }
}
