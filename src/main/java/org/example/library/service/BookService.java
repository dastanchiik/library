package org.example.library.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.library.dto.request.BookRequest;
import org.example.library.model.Book;
import org.example.library.model.BorrowRecord;
import org.example.library.model.User;
import org.example.library.repositories.BookRepository;
import org.example.library.repositories.BorrowRecordRepository;
import org.example.library.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowRecordRepository recordRepository;


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public String buyBook(Long userId,Long bookId) {
        User user = userRepository.findById(userId).get();
        Book book = bookRepository.findById(bookId).get();

        if (user.getBalance() < book.getPrice()) {
            throw new RuntimeException("Брат, не хватает баллов!");
        }

        user.setBalance(user.getBalance() - book.getPrice());

        BorrowRecord record = BorrowRecord.builder()
                .user(user)
                .book(book)
                .borrowDate(LocalDateTime.now())
                .isPurchased(true) // Помечаем, что куплено
                .lastReadPage(0)
                .build();

        recordRepository.save(record);
        userRepository.save(user);
        return "SOLD";
    }

    public Book addBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        return bookRepository.save(book);
    }
    public String updateBook(Long id, BookRequest book) {
        Book bookToUpdate = getBookById(id);
        bookToUpdate.setTitle(book.getTitle());
        bookToUpdate.setAuthor(book.getAuthor());
        bookRepository.save(bookToUpdate);
        return "Book updated";
    }
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
    }
}