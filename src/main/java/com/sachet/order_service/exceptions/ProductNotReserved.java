package com.sachet.order_service.exceptions;

public class ProductNotReserved extends RuntimeException {
    public ProductNotReserved(String message) {
        super(message);
    }
}
