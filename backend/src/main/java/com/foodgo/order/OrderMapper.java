package com.foodgo.order;

import com.foodgo.order.dto.OrderDto;
import com.foodgo.order.dto.OrderItemDto;
import com.foodgo.payment.Payment;
import com.foodgo.payment.PaymentMapper;
import com.foodgo.payment.PaymentRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public OrderMapper(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    public OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setRestaurantId(order.getRestaurant().getId());
        dto.setRestaurantName(order.getRestaurant().getName());
        dto.setDeliveryAddressLine(order.getDeliveryAddressLine());
        dto.setSubtotal(order.getSubtotal());
        dto.setDeliveryFee(order.getDeliveryFee());
        dto.setTax(order.getTax());
        dto.setDiscount(order.getDiscount());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());

        for (OrderItem item : order.getItems()) {
            OrderItemDto itemDto = new OrderItemDto();
            itemDto.setId(item.getId());
            itemDto.setFoodItemId(item.getFoodItem() != null ? item.getFoodItem().getId() : null);
            itemDto.setFoodItemName(item.getFoodItemName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setUnitPrice(item.getUnitPrice());
            itemDto.setLineTotal(item.getLineTotal());
            dto.getItems().add(itemDto);
        }

        paymentRepository.findByOrder(order).ifPresent(payment -> dto.setPayment(paymentMapper.toDto(payment)));

        return dto;
    }
}
