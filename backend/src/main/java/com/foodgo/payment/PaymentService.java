package com.foodgo.payment;

import com.foodgo.order.Order;

import java.math.BigDecimal;

public interface PaymentService {

    /**
     * Creates and immediately "processes" a mock payment for the given order.
     * No external payment gateway or API key is required: cash-on-delivery is
     * always PENDING, and mock card/UPI payments are simulated as SUCCESS.
     * The structure keeps a single seam ({@link #simulateGatewayCall}) where a
     * real payment provider integration could be plugged in later.
     */
    Payment createPaymentForOrder(Order order, PaymentMethod method, BigDecimal amount);
}
