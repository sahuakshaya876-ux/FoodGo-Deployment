package com.foodgo.restaurant.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class RestaurantRequest {

    @NotBlank(message = "Restaurant name is required")
    private String name;

    private String description;
    private String cuisineType;
    private String imageUrl;
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;
    private BigDecimal deliveryFee;
    private Integer estimatedDeliveryMinutes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Integer getEstimatedDeliveryMinutes() {
        return estimatedDeliveryMinutes;
    }

    public void setEstimatedDeliveryMinutes(Integer estimatedDeliveryMinutes) {
        this.estimatedDeliveryMinutes = estimatedDeliveryMinutes;
    }
}
