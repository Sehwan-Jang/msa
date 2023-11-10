package com.example.order.messagequeue;

import com.example.order.dto.OrderDto;

public record OrderCreateEvent(
        String topic
        , OrderDto orderDto
) {

}
