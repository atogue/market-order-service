package org.craftchain.market.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.craftchain.market.order.common.Constants;
import org.craftchain.market.order.common.Payment;
import org.craftchain.market.order.common.TransactionRequest;
import org.craftchain.market.order.common.TransactionResponse;
import org.craftchain.market.order.entity.Order;
import org.craftchain.market.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RefreshScope
public class OrderService {

    @Value("${microservice.payment-service.endpoints.endpoint.uri:http://PAYMENT-SERVICE/payment/doPayment}")
    private String ENDPOINT_URL;
    private final OrderRepository repository;
    private final RestTemplate template;
    private final Logger log = LoggerFactory.getLogger(OrderService.class);
    public OrderService(OrderRepository repository, @Lazy RestTemplate template) {
        this.repository = repository;
        this.template = template;
    }

    public TransactionResponse saveOrder(TransactionRequest request) throws JsonProcessingException {
        Order order = request.getOrder();
        Payment payment = request.getPayment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());
        order.setDate(Date.from(Instant.now())); // order date time

        log.info("OrderService request: {}", new ObjectMapper().writeValueAsString(request));
        Payment paymentResponse  = template.postForObject(ENDPOINT_URL, payment, Payment.class);
        log.info("OrderService : Payment Service response from rest template call : {}", new ObjectMapper().writeValueAsString(paymentResponse));
        TransactionResponse response;
        response = getTransactionResponse(order, paymentResponse);
        repository.save(order);
        return response;
    }

    private TransactionResponse getTransactionResponse(Order order, Payment paymentResponse) {
        TransactionResponse response;
        String responseMessage;
        if (paymentResponse != null) {
            if (paymentResponse.getStatus().equalsIgnoreCase(Constants.SUCCESS.name())) {
                order.setStatus(Constants.CONFIRMED.name().toLowerCase());
                responseMessage = "payment processing successful and order confirmed";
            } else {
                order.setStatus(Constants.IN_PROGRESS.name().toLowerCase());
                responseMessage = "payment failure from payment api and order added to cart, please try again!";
            }
           response = new TransactionResponse(order, paymentResponse.getAmount(), paymentResponse.getDate(), paymentResponse.getTransactionId(), responseMessage);
        } else {
            order.setStatus(Constants.DELAYED.name().toLowerCase());
            responseMessage = "payment api unavailable and order added to cart, please try again!";
            response = new TransactionResponse(order, 0, null, null, responseMessage);
        }
        return response;
    }

    public List<Order> findOrdersHistoryByClientId(int clientId) throws JsonProcessingException {
        var historyByClientId = repository.findAllByClientId(clientId);
        log.info("OrderService findOrdersHistoryByClientId : {}", new ObjectMapper().writeValueAsString(historyByClientId));
        return historyByClientId;
    }
}
