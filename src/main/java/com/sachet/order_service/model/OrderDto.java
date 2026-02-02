package com.sachet.order_service.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderDto {

    private String userId;
    private Status status;
    private Date expiresAt;
    @NotBlank(message = "productId cannot be blank!")
    private Long productId;
    private double price;
    private int count;

}
