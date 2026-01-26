package com.sachet.order_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "product")
public class Product {

    @Id
    private Long id;
    private String title;
    private double price;
    private int version;

}
