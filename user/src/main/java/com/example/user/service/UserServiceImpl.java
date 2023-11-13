package com.example.user.service;

import com.example.user.client.OrderServiceClient;
import com.example.user.domain.UserEntity;
import com.example.user.dto.ResponseOrder;
import com.example.user.dto.UserDto;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    public static final String ORDER_SERVICE_URL = "order_service.url";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final OrderServiceClient orderServiceClient;
    private final CircuitBreaker circuitBreaker;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(encoder.encode(userDto.getPwd()));

        UserEntity save = userRepository.save(userEntity);

        return modelMapper.map(save, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not Found"));
        UserDto userDto = modelMapper.map(userEntity, UserDto.class);
        /* use rest template */
//        String orderUrl = String.format(Objects.requireNonNull(environment.getProperty(ORDER_SERVICE_URL)), userId);
//        ResponseEntity<List<ResponseOrder>> response =
//                restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//                        new ParameterizedTypeReference<>() {
//                });
        /* use feign client */
//        List<ResponseOrder> orders = orderServiceClient.getOrders(userId);

        /* use circuit breaker */
        List<ResponseOrder> orders = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
                throwable -> new ArrayList<>());

        userDto.setOrders(orders);

        return userDto;
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userEntity -> modelMapper.map(userEntity, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());
    }
}
