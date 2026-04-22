package org.example.library.dto.response;

import lombok.*;
import org.example.library.model.Role;
import org.example.library.model.User;

//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private Double balance;
    private Role role;
    private String rank;
    private String favouriteGenre;
    private Integer totalBooks;
    private Long daysWithUs;

    public UserResponse fromEntity(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFullName(user.getFullName());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setBalance(user.getBalance());
        return userResponse;
    }
}
