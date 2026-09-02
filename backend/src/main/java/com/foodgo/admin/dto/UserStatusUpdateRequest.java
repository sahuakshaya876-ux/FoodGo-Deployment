package com.foodgo.admin.dto;

import jakarta.validation.constraints.NotNull;

public class UserStatusUpdateRequest {

    @NotNull(message = "Enabled flag is required")
    private Boolean enabled;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
