package com.foodgo.payment;

import com.foodgo.order.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment createPaymentForOrder(Order order, PaymentMethod method, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(method);
        payment.setAmount(amount);
        payment.setStatus(simulateGatewayCall(method));
        payment.setTransactionReference("FOODGO-" + UUID.randomUUID());
        return paymentRepository.save(payment);
    }

    /**
     * Placeholder for a real payment gateway call. Cash on delivery is settled
     * physically at delivery time, so it stays PENDING; mock card/UPI payments
     * are simulated as an instant SUCCESS since no real gateway is integrated yet.
     */
    private PaymentStatus simulateGatewayCall(PaymentMethod method) {
        if (method == PaymentMethod.CASH_ON_DELIVERY) {
            return PaymentStatus.PENDING;
        }
        return PaymentStatus.SUCCESS;
    }
}
