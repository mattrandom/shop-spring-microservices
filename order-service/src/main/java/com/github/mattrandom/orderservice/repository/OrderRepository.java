package com.github.mattrandom.orderservice.repository;

import com.github.mattrandom.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
