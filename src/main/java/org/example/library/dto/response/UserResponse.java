package org.example.library.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private Double balance;
    private String rank;
    private String favouriteGenre;
    private Integer totalBooks;
    private Long daysWithUs;
}
