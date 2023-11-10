package com.example.order.dto;

import lombok.Builder;

@Builder
public record Field(
        String type
        , boolean optional
        , String field
) {
}
