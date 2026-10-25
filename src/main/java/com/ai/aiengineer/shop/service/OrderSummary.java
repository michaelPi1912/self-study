package com.ai.aiengineer.shop.service;

import java.math.BigDecimal;

import com.ai.aiengineer.shop.domain.Order;
import com.ai.aiengineer.shop.domain.OrderItem;
import com.ai.aiengineer.shop.domain.OrderStatus;

public record OrderSummary(
        Long id,
        String orderNumber,
        String customerEmail,
        OrderStatus status,
        BigDecimal total,
        int itemCount,
        int totalQuantity) {

    public static OrderSummary from(Order order) {
        int quantity = order.getItems().stream().mapToInt(OrderItem::getQuantity).sum();
        return new OrderSummary(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomerEmail(),
                order.getStatus(),
                order.getTotal(),
                order.getItems().size(),
                quantity);
    }
}
