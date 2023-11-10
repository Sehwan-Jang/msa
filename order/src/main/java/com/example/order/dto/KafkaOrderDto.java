package com.example.order.dto;

import java.io.Serializable;

public record KafkaOrderDto(
    Schema schema,
    Payload payload
) implements Serializable {
}
