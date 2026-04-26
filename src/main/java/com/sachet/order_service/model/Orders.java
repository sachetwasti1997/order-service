package com.sachet.order_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "orders")
@ToString
public class Orders implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userId;
    private String status;
    private Date expiresAt;
    @NotNull(message = "productId cannot be null")
    @Positive(message = "Enter valid productId")
    private Long productId;
    private double price;
    private int count;
    private String sellerEmail;

}
