package com.example.catalog.controller;

import com.example.catalog.dto.CatalogDto;
import com.example.catalog.dto.ResponseCatalog;
import com.example.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/catalog-service")
@RestController
public class CatalogController {
    private final Environment environment;
    private final CatalogService catalogService;
    private ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in Catalog Service on PORT %s",
                environment.getProperty("local.server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
        Collection<CatalogDto> allCatalogs = catalogService.getAllCatalogs();
        List<ResponseCatalog> responseCatalogs = allCatalogs.stream()
                .map(dto -> modelMapper.map(dto, ResponseCatalog.class))
                .toList();
        return ResponseEntity.ok(responseCatalogs);
    }
}
