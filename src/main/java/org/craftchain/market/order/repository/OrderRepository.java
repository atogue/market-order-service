package org.craftchain.market.order.repository;

import org.craftchain.market.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByClientId(int clientId);
}
