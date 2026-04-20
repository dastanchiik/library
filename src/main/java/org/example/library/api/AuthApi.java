package org.example.library.api;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.example.library.dto.request.LoginRequest;
import org.example.library.dto.request.UserRegisterRequest;
import org.example.library.dto.response.JWTResponse;
import org.example.library.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController("/api/auth")
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

}
