package org.craftchain.market.order.service;

import org.craftchain.market.order.common.Payment;
import org.craftchain.market.order.common.TransactionRequest;
import org.craftchain.market.order.common.TransactionResponse;
import org.craftchain.market.order.entity.Order;
import org.craftchain.market.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Date;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;
    @Autowired
    private RestTemplate template;

    public TransactionResponse saveOrder(TransactionRequest request) {
        Order order = request.getOrder();
        Payment payment = request.getPayment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());
        order.setDate(Date.from(Instant.now())); // order date time

        Payment paymentResponse  = template.postForObject("http://localhost:9191/payment/doPayment", payment, Payment.class);
        TransactionResponse response;
        response = getTransactionResponse(order, paymentResponse);
        repository.save(order);
        return response;
    }

    private TransactionResponse getTransactionResponse(Order order, Payment paymentResponse) {
        TransactionResponse response;
        String responseMessage;
        if (paymentResponse != null) {
            if (paymentResponse.getStatus().equals("success")) {
                order.setStatus("confirmed");
                responseMessage = paymentResponse.getStatus().equals("success") ? "payment processing successful and order confirmed" : "payment failure from payment api and order added to cart";
            } else {
                order.setStatus("in progress");
                responseMessage = "payment failure from payment api and order added to cart, please try again!";
            }
           response = new TransactionResponse(order, paymentResponse.getAmount(), paymentResponse.getDate(), paymentResponse.getTransactionId(), responseMessage);
        } else {
            order.setStatus("delayed");
            responseMessage = "payment api unavailable and order added to cart, please try again!";
            response = new TransactionResponse(order, 0, null, null, responseMessage);
        }
        return response;
    }

}
