package org.example.library.dto.request;

import lombok.Data;
import org.example.library.model.Role;

@Data
public class UserRequest {
    private String fullName;
    private String username;
    private String email;
    private String password; // Сюда пароль будет падать только если его ввели
    private Role role;
    private Double balance;
}