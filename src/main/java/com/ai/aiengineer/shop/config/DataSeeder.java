package com.ai.aiengineer.shop.config;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ai.aiengineer.shop.domain.Order;
import com.ai.aiengineer.shop.domain.OrderItem;
import com.ai.aiengineer.shop.domain.OrderStatus;
import com.ai.aiengineer.shop.domain.Product;
import com.ai.aiengineer.shop.repository.OrderRepository;
import com.ai.aiengineer.shop.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final ProductRepository products;
    private final OrderRepository orders;

    @Override
    public void run(ApplicationArguments args) {
        if (products.count() > 0) {
            return;
        }
        List<Product> saved = products.saveAll(List.of(
                new Product("SKU-001", "Bàn phím cơ", new BigDecimal("89.00"), 50, new BigDecimal("52.00"), "NCC Hòa Phát, giá nhập có thể giảm 10% nếu đặt trên 200 cái"),
                new Product("SKU-002", "Chuột không dây", new BigDecimal("25.50"), 120, new BigDecimal("11.00"), "NCC Minh Long"),
                new Product("SKU-003", "Màn hình 27 inch", new BigDecimal("320.00"), 15, new BigDecimal("240.00"), "Hàng nhập khẩu, bảo hành 24 tháng"),
                new Product("SKU-004", "Sạc dự phòng 20000mAh", new BigDecimal("39.90"), 80, new BigDecimal("18.00"), "Lô 2026-03 có lỗi pin, đang thu hồi"),
                new Product("SKU-005", "Tai nghe chụp tai", new BigDecimal("149.00"), 30, new BigDecimal("90.00"), "NCC Âm Thanh Việt")));

        for (int i = 1; i <= 30; i++) {
            Order order = new Order();
            order.setOrderNumber("ORD-" + (900 + i));
            order.setCustomerEmail("khach" + i + "@example.com");
            order.setStatus(OrderStatus.PAID);
            order.setCreatedAt(Instant.now().minus(i, ChronoUnit.DAYS));
            BigDecimal total = BigDecimal.ZERO;
            for (int j = 0; j < 1 + i % 3; j++) {
                Product p = saved.get((i + j) % saved.size());
                int quantity = 1 + (i + j) % 4;
                order.addItem(new OrderItem(p.getSku(), quantity, p.getPrice()));
                total = total.add(p.getPrice().multiply(BigDecimal.valueOf(quantity)));
            }
            order.setTotal(total);
            orders.save(order);
        }
    }
}
