package org.example.library.api;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.example.library.dto.request.LoginRequest;
import org.example.library.dto.request.UserRegisterRequest;
import org.example.library.dto.response.JWTResponse;
import org.example.library.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final AuthService authService;

    @PermitAll
    @PostMapping("/register")
    public JWTResponse registrationPerson(@RequestBody UserRegisterRequest userRegisterRequest) {
        return authService.registerUser( userRegisterRequest );
    }

    @PostMapping("/login")
    @PermitAll
    public JWTResponse performLogin(@RequestBody LoginRequest loginResponse) {
        return authService.authenticate( loginResponse );
    }
    @PostMapping("admin/login") // путь будет /api/auth/login
    public ResponseEntity<?> adminLogin(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        // вызываешь сервис...
        return ResponseEntity.ok(authService.loginAdmin(email, password));
    }

}
