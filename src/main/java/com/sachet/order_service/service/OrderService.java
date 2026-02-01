package com.sachet.order_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.order_service.config.EnvironmentConfiguration;
import com.sachet.order_service.exceptions.*;
import com.sachet.order_service.model.*;
import com.sachet.order_service.repo.OrderRepository;
import com.sachet.order_service.repo.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductRepo productRepo;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EnvironmentConfiguration environmentConfiguration;

    public OrderService(OrderRepository orderRepository, ProductRepo productRepo, ObjectMapper objectMapper, JwtService jwtService, KafkaTemplate<String, String> kafkaTemplate, EnvironmentConfiguration environmentConfiguration) {
        this.orderRepository = orderRepository;
        this.productRepo = productRepo;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.kafkaTemplate = kafkaTemplate;
        this.environmentConfiguration = environmentConfiguration;
    }

    public List<Order> getAllOrderOfUser(String email, String bearerToken) {
        if (!jwtService.validateToken(email, bearerToken)) {
            throw new InvalidJwtException("The token is invalid!");
        }
        return orderRepository.getOrderByUserId(email);
    }

    public Optional<Order> getOrderById(String bearerToken, String email, long id) {
        if (!jwtService.validateToken(email, bearerToken)) {
            throw new InvalidJwtException("The token is invalid!");
        }
        return orderRepository.findById(id);
    }

    public void consumeProductCreatedEvent(ProductDto productDto) {
        Product product = objectMapper.convertValue(productDto, Product.class);
        LOGGER.info("Saving the product made: {}", product);
        productRepo.save(product);
    }

    public Order saveOrder(String bearerToken, String email, OrderDto orderDto) throws JsonProcessingException {
        if (!jwtService.validateToken(email, bearerToken)) {
            throw new InvalidJwtException("The token is invalid!");
        }
        long productId = orderDto.getProductId();
        // Find Product that user is trying to order
        Optional<Product> product = productRepo.findById(productId);
        // Determine product exists and not already reserved
        if (product.isEmpty()) {
            throw new ProductNotFound("The product not found!");
        }
        Product orderItem = product.get();
        if (orderItem.isReserved()) {
            throw new ProductAlreadyReserved("The product is already reserved and out of capacity");
        }
        orderItem.setReserved(true);
        //save the product with reserved flag
        productRepo.save(orderItem);
        //Calculate the expiration time for the order
        Date expiresAt = new Date();
        expiresAt.setTime(System.currentTimeMillis() + (2 * 60 * 60));
        //build order and save to database
        Order order = objectMapper.convertValue(orderDto, Order.class);
        order.setExpiresAt(expiresAt);
        order.setStatus(Status.ORDER_CREATED);
        //TODO: Publish event
        kafkaTemplate.send(environmentConfiguration.getTopics().get("order-created"),
                        objectMapper.writeValueAsString(order))
                .thenAccept(result -> {
                    LOGGER.info("Successfully sent the event {}", result);
                }).join();
        return orderRepository.save(order);
    }

    public Order cancelOrder(String bearerToken, String email, OrderDto orderDto) throws JsonProcessingException {
        if (!jwtService.validateToken(email, bearerToken)) {
            throw new InvalidJwtException("The token is invalid!");
        }
        if (!orderDto.getUserId().equalsIgnoreCase(email)) {
            throw new InvalidOrder("The order is not valid");
        }
        long productId = orderDto.getProductId();
        // Find Product that user is trying to order
        Optional<Product> product = productRepo.findById(productId);
        // Determine product exists and not already reserved
        if (product.isEmpty()) {
            throw new ProductNotFound("The product not found!");
        }
        Product orderItem = product.get();
        if (!orderItem.isReserved()) {
            throw new ProductNotReserved("The product is already reserved and out of capacity");
        }
        orderItem.setReserved(false);
        //save the product with reserved flag
        productRepo.save(orderItem);
        Order order = objectMapper.convertValue(orderDto, Order.class);
        order.setExpiresAt(null);
        order.setStatus(Status.ORDER_CANCELLED);
        //TODO: Publish event
        kafkaTemplate.send(environmentConfiguration.getTopics().get("order-cancelled"),
                objectMapper.writeValueAsString(order))
                .thenAccept(result -> {
                    LOGGER.info("Successfully sent the event {}", result);
                }).join();
        return orderRepository.save(order);
    }
}
