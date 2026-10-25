package com.ai.aiengineer.shop.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ai.aiengineer.shop.domain.Product;
import com.ai.aiengineer.shop.repository.ProductRepository;
import com.ai.aiengineer.shop.service.OrderService;
import com.ai.aiengineer.shop.service.OrderSummary;
import com.ai.aiengineer.shop.service.PaymentDeclinedException;
import com.ai.aiengineer.shop.service.PlaceOrderRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ShopController {

    private final OrderService orderService;
    private final ProductRepository products;

    @PostMapping("/orders")
    public ResponseEntity<OrderSummary> placeOrder(@RequestBody PlaceOrderRequest request)
            throws PaymentDeclinedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(request));
    }

    @GetMapping("/orders")
    public List<OrderSummary> listOrders() {
        return orderService.listOrders();
    }

    @GetMapping("/products/{sku}")
    public Product getProduct(@PathVariable String sku) {
        return products.findBySku(sku).orElseThrow(() -> new NoSuchElementException("Unknown SKU " + sku));
    }

    @ExceptionHandler(PaymentDeclinedException.class)
    public ResponseEntity<String> onPaymentDeclined(PaymentDeclinedException e) {
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> onNotFound(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> onConflict(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
