package com.sachet.order_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class OrderDto {

    private long id;
    private String userId;
    private Status status;
    private Date expiresAt;
    @NotNull(message = "productId cannot be null")
    @Positive(message = "Enter valid productId")
    private Long productId;
    private double price;
    private int count;

}
