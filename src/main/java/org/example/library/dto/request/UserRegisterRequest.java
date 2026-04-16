package org.example.library.dto.request;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserRegisterRequest {
    private String fullName;
    private String username;
    private String email;
    private String password;
}
