package com.sachet.order_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class Product {

    @Id
    private long id;
    private String title;
    private double price;
    private int version;
    private int count;
    private String imageUrl;
    @Column(name = "seller_email")
    private String email;

}
