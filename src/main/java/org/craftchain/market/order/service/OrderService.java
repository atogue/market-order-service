package org.craftchain.market.order.service;

import org.craftchain.market.order.entity.Order;
import org.craftchain.market.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    OrderRepository repository;

    public Order saveOrder(Order order) {
        return repository.save(order);
    }

}
