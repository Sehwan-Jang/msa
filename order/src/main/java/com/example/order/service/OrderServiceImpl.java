package com.example.order.service;

import com.example.order.domain.Order;
import com.example.order.dto.OrderDto;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

        Order order = modelMapper.map(orderDto, Order.class);
        Order saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderDto.class);
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not Found"));
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public Collection<OrderDto> getAllOrdersByUserId(String userId) {
        return orderRepository.findAllByUserId(userId).stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .toList();
    }
}
