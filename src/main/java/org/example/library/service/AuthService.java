package org.example.library.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.library.config.jwt.JwtUtils;
import org.example.library.dto.request.LoginRequest;
import org.example.library.dto.request.UserRegisterRequest;
import org.example.library.dto.response.JWTResponse;
import org.example.library.model.Role;
import org.example.library.model.User;
import org.example.library.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UserRepository repository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository repository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public JWTResponse registerUser(UserRegisterRequest userRegisterRequest) {
        User user = new User();
        user.setUsername(userRegisterRequest.getUsername());
        user.setFullName(userRegisterRequest.getFullName());
        user.setEmail(userRegisterRequest.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));

        if (repository.existsByEmail(userRegisterRequest.getEmail())) {
            throw new RuntimeException("The email " + userRegisterRequest.getEmail() + " is already in use!");
        }
        if (repository.existsByUsername(userRegisterRequest.getUsername())) {
            throw new RuntimeException("The username " + userRegisterRequest.getUsername() + " is already in use!");
        }
        User savedUser = repository.save(user);
        String token = jwtUtils.generateToken(userRegisterRequest.getEmail());

        return new JWTResponse(
                user.getId(),
                savedUser.getEmail(),
                token,
                "successfully registered",
                savedUser.getRole()

        );
    }
    public JWTResponse authenticate(LoginRequest loginRequest) {
        User user = new User();
//        repository.findByEmail(loginRequest.getEmail()).orElseThrow(() ->
//                new RuntimeException("User with email: " + loginRequest.getEmail() + " not found!"));
        if (repository.existsByEmail(loginRequest.getEmail())) {
            user = repository.findByEmail(loginRequest.getEmail()).orElseThrow(() ->
                    new RuntimeException("User with email: " + loginRequest.getEmail() + " not found!"));
        } else if (repository.existsByUsername(loginRequest.getEmail())) {
            user = repository.findByUsername(loginRequest.getEmail()).orElseThrow(() ->
                    new RuntimeException("User with email: " + loginRequest.getEmail() + " not found!"));
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String token = jwtUtils.generateToken(user.getEmail());
        return new JWTResponse(
                user.getId(),
                user.getEmail(),
                token,
                "Dastan",
                user.getRole()

        );
    }

    public String generateTokenForUser(String email) {
        return jwtUtils.generateToken(email);
    }

    public JWTResponse loginAdmin(String email, String password) {
        // 1. Проверка на email админа (хардкод допустим, если это единственная точка входа)
        if (!"admin@bookcloud.com".equalsIgnoreCase(email)) {
            throw new BadCredentialsException("Доступ запрещен: вы не администратор");
        }

        // 2. Ищем админа в базе
        User admin = repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Учетная запись администратора не найдена в системе"));

        // 3. Проверяем пароль
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new BadCredentialsException("Неверный пароль администратора");
        }

        // 4. Генерируем токен
        String token = jwtUtils.generateToken(admin.getEmail());

        return new JWTResponse(
                admin.getId(),
                admin.getEmail(),
                token,
                "Admin login successful",
                admin.getRole()
        );
    }
}