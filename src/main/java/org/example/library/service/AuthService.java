package org.example.library.service;

import lombok.RequiredArgsConstructor;
import org.example.library.config.jwt.JwtUtils;
import org.example.library.dto.request.LoginRequest;
import org.example.library.dto.request.UserRegisterRequest;
import org.example.library.dto.response.JWTResponse;
import org.example.library.model.Role;
import org.example.library.model.User;
import org.example.library.repositories.UserRepository;
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
        User user = new User(userRegisterRequest.getEmail());
        user.setUsername( userRegisterRequest.getFullName() );
        user.setEmail( userRegisterRequest.getEmail() );
        user.setRole( Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));

        if (repository.existsByEmail(userRegisterRequest.getEmail()))
            throw new RuntimeException("The email " + userRegisterRequest.getEmail() + " is already in use!");

        User savedUser = repository.save(user);
        String token = jwtUtils.generateToken(userRegisterRequest.getEmail());

        return new JWTResponse(
                savedUser.getEmail(),
                token,
                "Dastan",
                savedUser.getRole()

        );
}
    public JWTResponse authenticate(LoginRequest loginRequest) {
        User user = repository.findByEmail(loginRequest.getEmail()).orElseThrow(() ->
                new RuntimeException("User with email: " + loginRequest.getEmail() + " not found!"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String token = jwtUtils.generateToken(user.getEmail());
        return new JWTResponse(
                user.getEmail(),
                token,
                "Dastan",
                user.getRole()

        );
    }
}