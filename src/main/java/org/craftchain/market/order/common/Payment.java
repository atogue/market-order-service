package org.craftchain.market.order.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    private int paymentId;
    private String status;
    private String transactionId;
    private int orderId;
    private double amount;
    private Date date;
}
