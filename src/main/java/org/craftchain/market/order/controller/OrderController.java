package org.craftchain.market.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OrderController {

    private final OrderService service;
    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("/bookOrder")
    public TransactionResponse bookOrder(@RequestBody TransactionRequest request) {
        TransactionResponse response = null;
        try {
            response = service.saveOrder(request);
        } catch (JsonProcessingException err) {
            log.error("OrderService bookOrder ERROR : ", err);
        }
        return response;
    }

    @GetMapping("/{clientId}")
    public List<Order> findOrderHistoryByClientId(@PathVariable int clientId){
        List<Order> response = null;
        try {
            response = service.findOrdersHistoryByClientId(clientId);
        } catch (JsonProcessingException err) {
            log.error("OrderService findOrderHistoryByClientId ERROR : ", err);
        }
        return response;
    }
}
