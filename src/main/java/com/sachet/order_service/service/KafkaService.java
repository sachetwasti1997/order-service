package com.sachet.order_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.ProductDto;
import com.sachet.order_service.config.EnvironmentConfiguration;
import com.sachet.order_service.model.ProductEntity;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    @KafkaListener(topics = "${order.config.topics.add-product}", groupId = "${order.config.kafkaConfiguration.groupId}")
    public void consumer(ProductDto productDto) throws JsonProcessingException {

//        ProductDto productDto = objectMapper.readValue(data, ProductDto.class);
        ProductEntity productEntity = new ProductEntity();
        LOGGER.info("The product received: {}", productDto);
        productEntity.setId(productDto.getId());
        productEntity.setVersion(productDto.getVersion());
        productEntity.setPrice(productDto.getPrice());
        productEntity.setCount(productDto.getCount());
        productEntity.setEmail(productDto.getEmail() != null? productDto.getEmail().toString(): null);
        productEntity.setTitle(productDto.getTitle() != null ? productDto.getTitle().toString() : null);
        productEntity.setImageUrl(productDto.getImageUrl() != null ? productDto.getImageUrl().toString() : null);

        orderService.consumeProductCreatedEvent(productEntity);
    }

    @KafkaListener(topics = "update-product", groupId = "${order.config.kafkaConfiguration.groupId}")
    public void consumeUpdate(String data) throws JsonProcessingException {
        ProductEntity productEntity = objectMapper.readValue(data, ProductEntity.class);
//        LOGGER.info("The product received: {}", productDto);
        orderService.consumeProductUpdatedEvent(productEntity);
    }

}
