package com.example.catalog.repository;

import com.example.catalog.domain.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    Catalog findByProductId(String productId);
}
