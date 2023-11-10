package com.example.order.messagequeue;

import com.example.order.dto.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    @EventListener(OrderCreateEvent.class)
    public OrderDto send(OrderCreateEvent event) {
        OrderDto orderDto = event.orderDto();
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(orderDto);
        } catch (JsonProcessingException e) {
            log.error("error : ", e);
        }

        kafkaTemplate.send(event.topic(), jsonString);
        log.info("Kafka Producer sent data from Order Service : {}", jsonString);

        return orderDto;
    }
}
