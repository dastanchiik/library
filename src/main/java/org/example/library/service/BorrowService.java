package org.example.library.service;

import lombok.RequiredArgsConstructor;
import org.example.library.model.Book;
import org.example.library.model.BorrowRecord;
import org.example.library.model.User;
import org.example.library.repositories.BookRepository;
import org.example.library.repositories.BorrowRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;

    public BorrowService(BorrowRecordRepository borrowRecordRepository, BookRepository bookRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public BorrowRecord borrowBook(User user, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Книги нет в наличии");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);


        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setUser(user);
        record.setBorrowDate(LocalDateTime.now());

        return borrowRecordRepository.save(record);
    }
}