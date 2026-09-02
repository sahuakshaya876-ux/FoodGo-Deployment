package com.foodgo.user;

import com.foodgo.user.dto.UpdateProfileRequest;
import com.foodgo.user.dto.UserDto;

public interface UserService {
    UserDto getProfile(String email);
    UserDto updateProfile(String email, UpdateProfileRequest request);
    User getUserEntityByEmail(String email);
}
