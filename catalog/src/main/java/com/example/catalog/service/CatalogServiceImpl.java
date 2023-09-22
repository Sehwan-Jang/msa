package com.example.catalog.service;

import com.example.catalog.dto.CatalogDto;
import com.example.catalog.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CatalogServiceImpl implements CatalogService {
    private final CatalogRepository catalogRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public Collection<CatalogDto> getAllCatalogs() {
        return catalogRepository.findAll().stream()
                .map(catalog -> modelMapper.map(catalog, CatalogDto.class))
                .toList();
    }
}
