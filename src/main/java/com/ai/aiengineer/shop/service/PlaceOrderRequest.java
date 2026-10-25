package com.ai.aiengineer.shop.service;

import java.util.List;

public record PlaceOrderRequest(String customerEmail, List<Line> lines) {

    public record Line(String sku, int quantity) {
    }
}
