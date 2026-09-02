package com.foodgo.auth;

import com.foodgo.auth.dto.AuthResponse;
import com.foodgo.auth.dto.LoginRequest;
import com.foodgo.auth.dto.RegisterRequest;
import com.foodgo.exception.ConflictException;
import com.foodgo.security.JwtUtil;
import com.foodgo.user.Role;
import com.foodgo.user.User;
import com.foodgo.user.UserMapper;
import com.foodgo.user.UserRepository;
import com.foodgo.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setFullName("John Doe");
        registerRequest.setEmail("john@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole(Role.ROLE_CUSTOMER);
    }

    @Test
    void register_shouldCreateUserAndReturnToken_whenEmailNotTaken() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(registerRequest.getEmail());
        savedUser.setFullName(registerRequest.getFullName());
        savedUser.setRole(Role.ROLE_CUSTOMER);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().name())).thenReturn("mock-jwt-token");

        UserDto userDto = new UserDto();
        userDto.setEmail(savedUser.getEmail());
        when(userMapper.toDto(savedUser)).thenReturn(userDto);

        AuthResponse response = authService.register(registerRequest);

        assertThat(response.getToken()).isEqualTo("mock-jwt-token");
        assertThat(response.getUser().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void register_shouldThrowConflict_whenEmailAlreadyExists() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("password123");

        User user = new User();
        user.setEmail("john@example.com");
        user.setRole(Role.ROLE_CUSTOMER);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user.getEmail(), user.getRole().name())).thenReturn("mock-jwt-token");

        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        when(userMapper.toDto(user)).thenReturn(userDto);

        AuthResponse response = authService.login(loginRequest);

        assertThat(response.getToken()).isEqualTo("mock-jwt-token");
    }
}
