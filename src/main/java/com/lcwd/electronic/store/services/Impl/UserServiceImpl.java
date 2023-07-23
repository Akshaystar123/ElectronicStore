package com.lcwd.electronic.store.services.Impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.payload.AppConstants;
import com.lcwd.electronic.store.repositories.UserRepositoryI;
import com.lcwd.electronic.store.services.UserServiceI;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserServiceI {
    @Autowired
    private UserRepositoryI userRepositoryI;
    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imagePath;

    private UserDto entityToDto(User saveUser) {

        return mapper.map(saveUser, UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {

        return mapper.map(userDto, User.class);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Sending dao call to update user{}",userDto.getName());
        //generate userId in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        //dto to entity
        User user = dtoToEntity(userDto);
        User saveUser = userRepositoryI.save(user);
        //dto to entity
        UserDto newDto = entityToDto(saveUser);
        log.info("Sending dao call to update user{}",userDto.getName());
        return newDto;
    }
    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        log.info("Sending dao call to update user{}",userId);
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
        log.info("Completed dao call to successful update user{}",userId);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        log.info("Sending dao call to delete user{}",userId);
        User user = userRepositoryI.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND));
        String fullPath = imagePath + user.getImageName();
        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //delete user
        userRepositoryI.delete(user);
        log.info("Completed dao call to successful delete user{}",userId);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String SortDir) {
        log.info("Sending dao call to get all user");
        Sort sort = (sortBy.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> page = userRepositoryI.findAll(pageable);
        PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);
        log.info("Completed dao call to successful get all user");
        return response;

    }

    @Override
    public UserDto getUserById(String userId) {
        log.info("Sending dao call to get single user{}",userId);
        User user = userRepositoryI.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND));
        log.info("Completed dao call to successful get single user{}",userId);
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        log.info("Sending dao call to get single user by email{}",email);
        User user = userRepositoryI.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(AppConstants.ENTER_VALID_MAILID));
        log.info("Completed dao call to successful get single user by email{}",email);
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        log.info("Sending dao call to get single user by keyword{}",keyword);
        List<User> users = userRepositoryI.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        log.info("Completed dao call to successful get single user by keyword{}",keyword);
        return dtoList;
    }
}
