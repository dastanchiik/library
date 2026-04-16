package org.example.library.repositories;

import org.example.library.model.Book;
import org.example.library.model.BorrowRecord;
import org.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
        boolean existsByUserAndBook(User user, Book book);
}
