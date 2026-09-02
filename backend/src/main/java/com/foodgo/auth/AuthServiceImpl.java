package com.foodgo.auth;

import com.foodgo.auth.dto.AuthResponse;
import com.foodgo.auth.dto.LoginRequest;
import com.foodgo.auth.dto.RegisterRequest;
import com.foodgo.exception.ConflictException;
import com.foodgo.security.CustomUserDetails;
import com.foodgo.security.JwtUtil;
import com.foodgo.user.User;
import com.foodgo.user.UserMapper;
import com.foodgo.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                            AuthenticationManager authenticationManager, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("An account with this email already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());
        user.setEnabled(true);

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved.getEmail(), saved.getRole().name());
        return new AuthResponse(token, userMapper.toDto(saved));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ConflictException("Invalid email or password"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, userMapper.toDto(user));
    }
}
