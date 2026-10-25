package com.ai.aiengineer.shop.domain;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String name;
    private BigDecimal price;
    private int stock;
    private BigDecimal costPrice;
    private String supplierNote;

    public Product(String sku, String name, BigDecimal price, int stock, BigDecimal costPrice, String supplierNote) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.costPrice = costPrice;
        this.supplierNote = supplierNote;
    }
}
