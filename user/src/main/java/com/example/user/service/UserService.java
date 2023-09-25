package com.example.user.service;

import com.example.user.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);
    Collection<UserDto> getAllUsers();

    UserDto getUserDetailsByEmail(String email);
}
