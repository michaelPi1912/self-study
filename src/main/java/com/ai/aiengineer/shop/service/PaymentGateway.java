package com.ai.aiengineer.shop.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

/** Cổng thanh toán giả lập: từ chối mọi giao dịch trên 1000. */
@Component
public class PaymentGateway {

    private static final BigDecimal LIMIT = new BigDecimal("1000");

    public void charge(String customerEmail, BigDecimal amount) throws PaymentDeclinedException {
        if (amount.compareTo(LIMIT) > 0) {
            throw new PaymentDeclinedException("Payment declined for " + customerEmail + ": amount " + amount);
        }
    }
}
