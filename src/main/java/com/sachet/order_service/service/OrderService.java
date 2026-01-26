package com.sachet.order_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.order_service.model.Product;
import com.sachet.order_service.model.ProductDto;
import com.sachet.order_service.repo.OrderRepository;
import com.sachet.order_service.repo.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductRepo productRepo;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository, ProductRepo productRepo, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.productRepo = productRepo;
        this.objectMapper = objectMapper;
    }

    public void consumeProductCreatedEvent(ProductDto productDto) {
        Product product = objectMapper.convertValue(productDto, Product.class);
        LOGGER.info("Saving the product made: {}", product);
        productRepo.save(product);
    }
}
