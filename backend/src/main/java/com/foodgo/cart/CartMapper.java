package com.foodgo.cart;

import com.foodgo.cart.dto.CartDto;
import com.foodgo.cart.dto.CartItemDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartMapper {

    public CartDto toDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());

        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            CartItemDto itemDto = new CartItemDto();
            itemDto.setId(item.getId());
            itemDto.setFoodItemId(item.getFoodItem().getId());
            itemDto.setFoodItemName(item.getFoodItem().getName());
            itemDto.setFoodItemImageUrl(item.getFoodItem().getImageUrl());
            itemDto.setPrice(item.getFoodItem().getPrice());
            itemDto.setQuantity(item.getQuantity());

            BigDecimal lineTotal = item.getFoodItem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            itemDto.setLineTotal(lineTotal);
            subtotal = subtotal.add(lineTotal);

            dto.getItems().add(itemDto);
        }

        dto.setSubtotal(subtotal);
        return dto;
    }
}
