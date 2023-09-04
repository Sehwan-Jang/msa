package com.example.user.controller;

import com.example.user.dto.ResponseUser;
import com.example.user.dto.UserDto;
import com.example.user.service.UserService;
import com.example.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class UserController {
    private final UserService service;
    private final Greeting greeting;

    @GetMapping("/health_check")
    public String status() {
        return "It's Working in User Service";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser request) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(request, UserDto.class);
        UserDto user = service.createUser(userDto);

        return ResponseEntity.created(URI.create(user.getUserId()))
                .body(modelMapper.map(userDto, ResponseUser.class));
    }
}
