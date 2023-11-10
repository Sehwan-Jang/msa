package com.example.order.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record Schema(
        String type,
        List<Field> fields,
        boolean optional,
        String name
) {
}
