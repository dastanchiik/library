package org.example.library.api;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.request.BookRequest;
import org.example.library.dto.response.BookResponse;
import org.example.library.model.Book;
import org.example.library.model.Category;
import org.example.library.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookApi {
    private final BookService bookService;

    @GetMapping("/books")
    public List<BookResponse> getBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping("/save/book")
    public String saveBook(@RequestBody BookRequest book,@RequestParam Category category) {
        book.setCategory(category);
        bookService.addBook(book);
        return "successfully saved";
    }

    @PatchMapping("/updateBook")
    public String updateBook(@RequestParam Long id, @RequestBody BookRequest book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping
    public String deleteBook(@RequestParam Long id) {
        bookService.deleteBook(id);
        return "successfully deleted";
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/search/book/title")
    public List<BookResponse> search(@RequestParam String query) {
        return bookService.searchBooks(query)
                .stream()
                .map(BookResponse::fromEntity) // Превращаем каждую книгу в Response
                .toList();
    }

    @GetMapping("/search/books/category")
    public List<BookResponse> getBooksByCategory(@RequestParam Category category) {
        return bookService.searchBooksByCategory(category);
    }
}