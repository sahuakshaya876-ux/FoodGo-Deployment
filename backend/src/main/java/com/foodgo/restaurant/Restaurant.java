package com.foodgo.restaurant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodgo.common.BaseEntity;
import com.foodgo.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Entity
@Table(name = "restaurants")
public class Restaurant extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, unique = true)
    @JsonIgnore
    private User owner;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String description;

    private String cuisineType;

    private String imageUrl;

    private String address;

    private String city;

    private Double latitude;

    private Double longitude;

    @Column(nullable = false)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer totalReviews = 0;

    @Column(nullable = false)
    private BigDecimal deliveryFee = BigDecimal.valueOf(40);

    @Column(nullable = false)
    private Integer estimatedDeliveryMinutes = 35;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantStatus status = RestaurantStatus.PENDING_APPROVAL;

    @Column(nullable = false)
    private boolean open = true;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

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

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
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

    public RestaurantStatus getStatus() {
        return status;
    }

    public void setStatus(RestaurantStatus status) {
        this.status = status;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
