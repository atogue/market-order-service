package org.craftchain.market.order.service;

import org.craftchain.market.order.entity.Order;
import org.craftchain.market.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    public Order saveOrder(Order order) {
        order.setDate(Date.from(Instant.now())); // order date time
        return repository.save(order);
    }

}
