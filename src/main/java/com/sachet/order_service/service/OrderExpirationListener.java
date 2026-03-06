package com.sachet.order_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sonus21.rqueue.annotation.RqueueListener;
import com.sachet.order_service.config.EnvironmentConfiguration;
import com.sachet.order_service.model.Orders;
import com.sachet.order_service.model.Product;
import com.sachet.order_service.model.Status;
import com.sachet.order_service.repo.OrderRepository;
import com.sachet.order_service.repo.ProductRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class OrderExpirationListener {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderRepository orderRepository;
    private final ProductRepo productRepo;
    private final EnvironmentConfiguration configuration;
    private final ObjectMapper objectMapper;

    public OrderExpirationListener(KafkaTemplate<String, String> kafkaTemplate, OrderRepository orderRepository, ProductRepo productRepo, EnvironmentConfiguration configuration, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderRepository = orderRepository;
        this.productRepo = productRepo;
        this.configuration = configuration;
        this.objectMapper = objectMapper;
    }

    @RqueueListener(value = "order-expiration-queue")
    public void cancelOrder(Orders orders) throws JsonProcessingException {
        try {
            log.info("Entered into order expiration listener with order {}", orders);
            if (orders.getStatus().equals(Status.PAYMENT_COMPLETED)) {
                log.info("The payment is completed for the order!");
                return;
            }
            long productId = orders.getProductId();
            // Find Product that user is trying to order
            Optional<Product> product = productRepo.findById(productId);
            Product orderItem = product.get();
//            //save the product with reserved flag
            orderItem.setCount(orderItem.getCount() + orders.getCount());
//
            orders.setExpiresAt(null);
            orders.setStatus(Status.ORDER_CANCELLED.name());
            orders.setSellerEmail(orderItem.getEmail());
            orderRepository.updateOrdersById(Status.ORDER_CANCELLED.name(), orders.getId());
            productRepo.save(orderItem);
            kafkaTemplate.send(configuration.getTopics().get("order-cancelled"),
                            objectMapper.writeValueAsString(orders))
                    .thenAccept(result -> {
                        log.info("Successfully sent the event {}", result);
                    }).join();
        } catch (Exception e) {
            log.error("Ex: {}", e.getMessage());
        }
    }

}
