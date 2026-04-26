package com.sachet.order_service.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductEntity {

    private Long id;
    private String title;
    private double price;
    private int version;
    private int count;
    private String imageUrl;
    private String email;

}
