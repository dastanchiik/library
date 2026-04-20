package org.example.library.service;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.request.BookRequest;
import org.example.library.dto.response.BookResponse;
import org.example.library.model.*;
import org.example.library.repositories.BookRepository;
import org.example.library.repositories.OrderRepository;
import org.example.library.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookResponse::fromEntity) // Превращаем каждую книгу в Response
                .toList();
    }

    // Твой генератор ISBN - отличная фишка!
    private String generateIsbn(Random random) {
        return String.format("%03d-%d-%07d-%d-%d",
                random.nextInt(101, 999),
                random.nextInt(10),
                random.nextInt(1000000, 9999999),
                random.nextInt(10),
                random.nextInt(10));
    }

    public Book addBook(BookRequest bookRequest) {
        Book book = new Book();
        Random random = new Random();
        String isbn;

        while (true) {
            isbn = generateIsbn(random);
            if (!bookRepository.existsByIsbn(isbn)) {
                break;
            }
        }

        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setPrice(bookRequest.getPrice());
        book.setCategory(bookRequest.getCategory());
        book.setImageURL(bookRequest.getImageUrl());
        book.setDescription(bookRequest.getDescription());
        book.setStock(bookRequest.getStock()); // Не забудь добавить в BookRequest поле stock
        book.setIsbn(isbn);

        return bookRepository.save(book);
    }

    public String updateBook(Long id, BookRequest bookRequest) {
        Book book = getBookById(id);
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setPrice(bookRequest.getPrice());
        book.setCategory(bookRequest.getCategory());
        book.setImageURL(bookRequest.getImageUrl());
        book.setDescription(bookRequest.getDescription());
        book.setStock(bookRequest.getStock()); // И наличие
        bookRepository.save(book);
        return "Book updated";
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
    }

    public List<Book> searchBooks(String title){
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<BookResponse> searchBooksByCategory(Category category){
        return bookRepository.findAllByCategory(category)
                .stream()
                .map(BookResponse::fromEntity) // Превращаем каждую книгу в Response
                .toList();
    }
}

