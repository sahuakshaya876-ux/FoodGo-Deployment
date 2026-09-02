package com.foodgo.cart.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartDto {
    private Long id;
    private List<CartItemDto> items = new ArrayList<>();
    private BigDecimal subtotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
