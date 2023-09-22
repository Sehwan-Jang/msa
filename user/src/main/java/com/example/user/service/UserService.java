package com.example.user.service;

import com.example.user.domain.User;
import com.example.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);
    Collection<UserDto> getAllUsers();
}
