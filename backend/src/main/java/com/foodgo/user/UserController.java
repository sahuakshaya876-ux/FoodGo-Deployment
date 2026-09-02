package com.foodgo.user;

import com.foodgo.common.ApiResponse;
import com.foodgo.user.dto.UpdateProfileRequest;
import com.foodgo.user.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ApiResponse<UserDto> getProfile(Authentication authentication) {
        UserDto dto = userService.getProfile(authentication.getName());
        return ApiResponse.success("Profile fetched successfully", dto);
    }

    @PutMapping("/me")
    public ApiResponse<UserDto> updateProfile(Authentication authentication,
                                               @Valid @RequestBody UpdateProfileRequest request) {
        UserDto dto = userService.updateProfile(authentication.getName(), request);
        return ApiResponse.success("Profile updated successfully", dto);
    }
}
