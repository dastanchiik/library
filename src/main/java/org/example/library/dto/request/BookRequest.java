package org.example.library.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.example.library.model.Category;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class BookRequest {
    private String title;
    private String author;
    private int price;
    @JsonIgnore
    private Category category;
    private String imageUrl;
    private String description;
    private String isbn;
    private int stock;
}
