package com.ai.aiengineer.shop.service;

public class PaymentDeclinedException extends Exception {

    public PaymentDeclinedException(String message) {
        super(message);
    }
}
