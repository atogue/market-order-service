package org.craftchain.market.order.controller;

import org.craftchain.market.order.common.TransactionRequest;
import org.craftchain.market.order.common.TransactionResponse;
import org.craftchain.market.order.entity.Order;
import org.craftchain.market.order.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService service;
    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("/bookOrder")
    public TransactionResponse bookOrder(@RequestBody TransactionRequest request) {
        return  service.saveOrder(request);
    }

    @GetMapping("/{clientId}")
    public List<Order> findOrderHistoryByClientId(@PathVariable int clientId){
        return service.findOrdersHistoryByClientId(clientId);
    }
}
