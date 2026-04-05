package com.sachet.order_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.order_service.config.EnvironmentConfiguration;
import com.sachet.order_service.model.ProductDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaService.class);

    private final EnvironmentConfiguration environmentConfiguration;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public KafkaService(EnvironmentConfiguration environmentConfiguration, OrderService orderService) {
        this.environmentConfiguration = environmentConfiguration;
        this.orderService = orderService;
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @KafkaListener(topics = "${order.config.topics.add-product}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumer(ConsumerRecord<String, com.sachet.order_service.model.dto.ProductDto> consumerRecord) throws JsonProcessingException {
        ProductDto productDto = objectMapper.convertValue(consumerRecord.value(), ProductDto.class);
//        LOGGER.info("The product received: {}", productDto);
        orderService.consumeProductCreatedEvent(productDto);
    }

    @KafkaListener(topics = "update-product", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUpdate(String data) throws JsonProcessingException {
        ProductDto productDto = objectMapper.readValue(data, ProductDto.class);
//        LOGGER.info("The product received: {}", productDto);
        orderService.consumeProductUpdatedEvent(productDto);
    }

}
