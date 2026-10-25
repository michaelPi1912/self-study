package com.ai.aiengineer.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ai.aiengineer.shop.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
