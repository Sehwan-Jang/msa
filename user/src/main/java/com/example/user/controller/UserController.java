package com.example.user.controller;

import com.example.user.dto.ResponseUser;
import com.example.user.dto.UserDto;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService service;
    private final Greeting greeting;
    private final Environment environment;
    private ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service" +
                " PORT(local) = " + environment.getProperty("local.server.port") +
                " PORT(server) = " + environment.getProperty("server.port") +
                " TOKEN SECRET = " + environment.getProperty("token.secret") +
                " TOKEN EXPIRATION TIME = " + environment.getProperty("token.expiration_time"))
                ;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser request) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(request, UserDto.class);
        UserDto user = service.createUser(userDto);

        return ResponseEntity.created(URI.create(user.getUserId()))
                .body(modelMapper.map(userDto, ResponseUser.class));
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        Collection<UserDto> allUsers = service.getAllUsers();
        List<ResponseUser> responseUsers = allUsers.stream()
                .map(dto -> modelMapper.map(dto, ResponseUser.class))
                .toList();
        return ResponseEntity.ok(responseUsers);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
        modelMapper.typeMap(UserDto.class, ResponseUser.class)
                .addMapping(UserDto::getOrders, ResponseUser::setResponseOrders);
        UserDto userDto = service.getUserByUserId(userId);
        ResponseUser responseUser = modelMapper.map(userDto, ResponseUser.class);
        return ResponseEntity.ok(responseUser);
    }
}
