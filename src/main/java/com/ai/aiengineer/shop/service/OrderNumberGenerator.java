package com.ai.aiengineer.shop.service;

import org.springframework.stereotype.Component;

@Component
public class OrderNumberGenerator {

    private int counter = 1000;

    public String next() {
        counter++;
        return "ORD-" + counter;
    }
}
