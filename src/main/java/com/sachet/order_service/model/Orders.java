package com.sachet.order_service.model;

import jakarta.persistence.*;
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
    private Long productId;
    private double price;
    private int count;
    private String sellerEmail;

}
