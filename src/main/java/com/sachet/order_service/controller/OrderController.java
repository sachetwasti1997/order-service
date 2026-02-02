package com.sachet.order_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sachet.order_service.model.Orders;
import com.sachet.order_service.model.OrderDto;
import com.sachet.order_service.service.JwtService;
import com.sachet.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final JwtService jwtService;

    public OrderController(OrderService orderService, JwtService jwtService) {
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    @GetMapping("/all-orders")
    public List<Orders> getAllOrders(@RequestHeader("Authorization")String bearerToken,
                                     @RequestParam(name = "email") String email) {
        return orderService.getAllOrderOfUser(email, bearerToken);
    }

    @GetMapping("/order")
    public Optional<Orders> getOrderById(@RequestHeader("Authorization")String bearerToken, @RequestParam(name = "email")String email, @RequestParam(name = "id")long id) {
        return orderService.getOrderById(bearerToken, email, id);
    }

    @PostMapping("/create")
    public Orders createOrder(@RequestHeader("Authorization")String bearerToken,
                              @RequestParam(name = "email")String email,
                              @RequestBody @Valid OrderDto orderDto) throws JsonProcessingException {
        return orderService.saveOrder(bearerToken, email, orderDto);
    }

    @PutMapping("/cancel")
    public Orders cancelOrder(@RequestHeader("Authorization")String bearerToken,
                              @RequestParam(name = "email")String email,
                              @RequestBody @Valid OrderDto orderDto) throws JsonProcessingException {
        return orderService.cancelOrder(bearerToken, email, orderDto);
    }
}
