package org.example.library.api;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.request.BookRequest;
import org.example.library.model.Book;
import org.example.library.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/book")
@RequiredArgsConstructor
public class BookApi {
    private final BookService bookService;

    @GetMapping("/books")
    public List<Book> getBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping("/save/book")
    public String saveBook(BookRequest book) {
        bookService.addBook(book);
        return "successfully saved";
    }



}