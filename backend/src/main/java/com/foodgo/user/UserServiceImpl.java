package com.foodgo.user;

import com.foodgo.exception.ResourceNotFoundException;
import com.foodgo.user.dto.UpdateProfileRequest;
import com.foodgo.user.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto getProfile(String email) {
        return userMapper.toDto(getUserEntityByEmail(email));
    }

    @Override
    public UserDto updateProfile(String email, UpdateProfileRequest request) {
        User user = getUserEntityByEmail(email);
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public User getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
