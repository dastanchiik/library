package org.example.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.library.model.Role;

@AllArgsConstructor
@Getter
@Setter
public class JWTResponse {
    private Long id;
    private String email;
    private String token;
    private String message;
    private Role role;

}
