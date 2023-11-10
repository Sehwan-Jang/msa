package com.example.catalog.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "catalog")
@Entity
public class Catalog extends BaseEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120, unique = true)
    private String productId;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private Integer stock;
    @Column(nullable = false)
    private Integer unitPrice;

    public void sold(int qty) {
        this.stock -= qty;
    }
}
