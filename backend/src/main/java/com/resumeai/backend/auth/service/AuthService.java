package com.resumeai.backend.auth.service;

import com.resumeai.backend.auth.dto.AuthResponse;
import com.resumeai.backend.auth.dto.CurrentUserResponse;
import com.resumeai.backend.auth.dto.LoginRequest;
import com.resumeai.backend.auth.dto.RegisterRequest;
import com.resumeai.backend.auth.dto.RegisterResponse;
import com.resumeai.backend.auth.security.JwtService;
import com.resumeai.backend.common.exception.DuplicateEmailException;
import com.resumeai.backend.common.exception.InvalidCredentialsException;
import com.resumeai.backend.user.entity.Role;
import com.resumeai.backend.user.entity.User;
import com.resumeai.backend.user.repository.RoleRepository;
import com.resumeai.backend.user.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        if (!"CANDIDATE".equals(request.role()) && !"RECRUITER".equals(request.role())) {
            throw new IllegalArgumentException("Only CANDIDATE and RECRUITER roles may be registered");
        }

        // 1. Check whether email already exists
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }

        // 2. Find the requested role
        Role role = roleRepository.findByName(request.role())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid user role"
                        )
                );

        // 3. Create a new user
        User user = new User();

        user.setFullName(request.fullName().trim());
        user.setEmail(email);

        // 4. Hash the password using BCrypt
        user.setPassword(
                passwordEncoder.encode(request.password())
        );

        // 5. Assign the role
        user.setRole(role);

        // 6. Save the user in PostgreSQL
        User savedUser = userRepository.save(user);

        // 7. Return safe user information
        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getRole().getName(),
                "Registration successful"
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(normalizeEmail(request.email()))
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return toAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public CurrentUserResponse currentUser(String email) {
        User user = userRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(InvalidCredentialsException::new);
        return new CurrentUserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole().getName());
    }

    private AuthResponse toAuthResponse(User user) {
        String role = user.getRole().getName();
        return new AuthResponse(
                jwtService.generateToken(user.getId(), user.getEmail(), role),
                "Bearer",
                user.getId(), user.getFullName(), user.getEmail(), role);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
