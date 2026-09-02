package com.foodgo.payment;

import com.foodgo.payment.dto.PaymentDto;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentDto toDto(Payment payment) {
        PaymentDto dto = new PaymentDto();
        dto.setId(payment.getId());
        dto.setMethod(payment.getMethod());
        dto.setStatus(payment.getStatus());
        dto.setAmount(payment.getAmount());
        dto.setTransactionReference(payment.getTransactionReference());
        return dto;
    }
}
