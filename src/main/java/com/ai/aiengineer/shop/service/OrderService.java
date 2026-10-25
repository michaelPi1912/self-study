package com.ai.aiengineer.shop.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.aiengineer.shop.domain.Order;
import com.ai.aiengineer.shop.domain.OrderItem;
import com.ai.aiengineer.shop.domain.OrderStatus;
import com.ai.aiengineer.shop.domain.Product;
import com.ai.aiengineer.shop.repository.OrderRepository;
import com.ai.aiengineer.shop.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orders;
    private final ProductRepository products;
    private final OrderNumberGenerator numbers;
    private final RecallList recallList;
    private final PaymentGateway paymentGateway;

    @Transactional
    public OrderSummary placeOrder(PlaceOrderRequest request) throws PaymentDeclinedException {
        Order order = new Order();
        order.setOrderNumber(numbers.next());
        order.setCustomerEmail(request.customerEmail());
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(Instant.now());

        BigDecimal total = BigDecimal.ZERO;
        for (PlaceOrderRequest.Line line : request.lines()) {
            Product product = products.findBySku(line.sku())
                    .orElseThrow(() -> new NoSuchElementException("Unknown SKU " + line.sku()));
            if (recallList.isRecalled(line.sku())) {
                throw new IllegalStateException("SKU " + line.sku() + " has been recalled");
            }
            if (product.getStock() < line.quantity()) {
                throw new IllegalStateException("Not enough stock for " + line.sku());
            }
            order.addItem(new OrderItem(line.sku(), line.quantity(), product.getPrice()));
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(line.quantity())));
        }
        order.setTotal(total);
        orders.save(order);

        paymentGateway.charge(order.getCustomerEmail(), total);
        order.setStatus(OrderStatus.PAID);
        return OrderSummary.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderSummary> listOrders() {
        return orders.findAll().stream().map(OrderSummary::from).toList();
    }
}
