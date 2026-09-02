package com.foodgo.user.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateProfileRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    private String phoneNumber;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
