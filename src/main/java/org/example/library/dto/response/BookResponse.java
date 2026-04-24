package org.example.library.dto.response;

import lombok.Data;
import org.example.library.model.Book;

@Data
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private Double price;
    private String category;
    private Integer stock;
    private Integer salesCount;
    private String imageURL;
    private String description;

    public static BookResponse fromEntity(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setDescription(book.getDescription());
        response.setPrice(book.getPrice());
        response.setStock(book.getStock());
        response.setCategory(book.getCategory().name());
        response.setSalesCount(book.getSalesCount());
        response.setImageURL(book.getImageURL());
        return response;
    }
}
