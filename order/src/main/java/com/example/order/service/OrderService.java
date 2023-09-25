package com.example.order.service;

import com.example.order.dto.OrderDto;

import java.util.Collection;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDetails);

    OrderDto getOrderByOrderId(String orderId);
    Collection<OrderDto> getAllOrdersByUserId(String userId);
}
