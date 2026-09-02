package com.foodgo.order.dto;

import com.foodgo.payment.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PlaceOrderRequest {

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddressLine;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private String discountCode;

    public String getDeliveryAddressLine() {
        return deliveryAddressLine;
    }

    public void setDeliveryAddressLine(String deliveryAddressLine) {
        this.deliveryAddressLine = deliveryAddressLine;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }
}
