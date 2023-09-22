package com.example.catalog.service;

import com.example.catalog.dto.CatalogDto;

import java.util.Collection;

public interface CatalogService {
    Collection<CatalogDto> getAllCatalogs();
}
