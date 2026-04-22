package org.example.library.dto.response;

import lombok.Data;
import org.example.library.model.Book;

@Data
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private Double price;
    private String category; // Здесь будет русское название
    private Integer stock;

    // Статический метод, чтобы быстро превратить Entity в DTO
    public static BookResponse fromEntity(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setPrice(book.getPrice());
        response.setStock(book.getStock());
        response.setCategory(book.getCategory().name());
        return response;
    }
}
