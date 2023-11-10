package com.example.order.controller;

import com.example.order.dto.OrderDto;
import com.example.order.dto.RequestOrder;
import com.example.order.dto.ResponseOrder;
import com.example.order.messagequeue.KafkaProducer;
import com.example.order.messagequeue.OrderCreateEvent;
import com.example.order.messagequeue.OrderProducer;
import com.example.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/order-service")
@RestController
public class OrderController {
    private final Environment environment;
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final OrderProducer producer;

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in Order Service on PORT %s",
                environment.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder orderDetails) {
        OrderDto dto = modelMapper.map(orderDetails, OrderDto.class);
        dto.setUserId(userId);

//        OrderDto created = orderService.createOrder(dto);
//        ResponseOrder responseOrder = modelMapper.map(created, ResponseOrder.class);

        dto.setOrderId(UUID.randomUUID().toString());
        dto.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());

        OrderDto order = producer.send("orders", dto);
        ResponseOrder responseOrder = modelMapper.map(order, ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) {
        List<ResponseOrder> orders = orderService.getAllOrdersByUserId(userId).stream()
                .map(order -> modelMapper.map(order, ResponseOrder.class))
                .toList();
        return ResponseEntity.ok(orders);
    }

}
