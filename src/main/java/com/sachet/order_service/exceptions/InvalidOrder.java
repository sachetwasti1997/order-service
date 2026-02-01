package com.sachet.order_service.exceptions;

public class InvalidOrder extends RuntimeException {
    public InvalidOrder(String message) {
        super(message);
    }
}
