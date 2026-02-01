package com.sachet.order_service.exceptions;

public class ProductAlreadyReserved extends RuntimeException {
    public ProductAlreadyReserved(String message) {
        super(message);
    }
}
