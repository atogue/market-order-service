package org.craftchain.market.order.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.craftchain.market.order.entity.Order;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private Order order;
    private double amount;
    private Date paymentDate;
    private String transactionId;
    private String message;
}
