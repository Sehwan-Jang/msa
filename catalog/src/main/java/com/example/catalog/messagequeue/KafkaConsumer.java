package com.example.catalog.messagequeue;

import com.example.catalog.domain.Catalog;
import com.example.catalog.repository.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaConsumer {

    private final CatalogRepository repository;
    private final ObjectMapper objectMapper;
    
    @KafkaListener(topics = "example-catalog-topic") 
    public void updateQuantity(String message) {
        log.info("kafka Message : {}", message);

        Map<Object, Object> map = new HashMap<>();
        try {
            objectMapper.readValue(message, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException e) {
            log.error("error : ", e);
        }

        Catalog catalog = repository.findByProductId((String) map.get("productId"));
        if (catalog != null) {
            catalog.sold((Integer) map.get("qty"));
        }
    }
}
