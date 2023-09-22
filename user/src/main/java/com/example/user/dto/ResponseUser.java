package com.example.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResponseUser {

    private String name;
    private String email;
    private String userId;
    private List<ResponseOrder> responseOrders;
}
