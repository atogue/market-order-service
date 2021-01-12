package org.craftchain.market.order.controller;

import org.craftchain.market.order.entity.Order;
import org.craftchain.market.order.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    OrderService service;

    @PostMapping("/bookOrder")
    public Order bookOrder(@RequestBody Order order) {
        return  service.saveOrder(order);
    }
}
