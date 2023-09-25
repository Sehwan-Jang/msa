package com.example.catalog.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class CatalogDto implements Serializable {
    private String productId;
    private String productName;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private String orderId;
    private String userId;
    private Integer Stock;
    private Date createdAt;
}
