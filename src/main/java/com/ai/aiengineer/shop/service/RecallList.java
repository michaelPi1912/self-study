package com.ai.aiengineer.shop.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ai.aiengineer.shop.domain.Sku;

/** Sản phẩm bị thu hồi, không được phép bán. */
@Component
public class RecallList {

    private final Set<Sku> recalled = new HashSet<>();

    public RecallList() {
        recalled.add(new Sku("SKU-004"));
    }

    public boolean isRecalled(String code) {
        return recalled.contains(new Sku(code));
    }
}
