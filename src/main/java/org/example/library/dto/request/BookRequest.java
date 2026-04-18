package org.example.library.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class BookRequest {
    private String title;
    private String author;
    private int price;
    private String isbn;
    private int stock;
}
