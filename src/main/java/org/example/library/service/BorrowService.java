package org.example.library.service;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.request.UserRegisterRequest;
import org.example.library.model.Book;
import org.example.library.model.BorrowRecord;
import org.example.library.model.User;
import org.example.library.repositories.BookRepository;
import org.example.library.repositories.BorrowRecordRepository;
import org.example.library.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public BorrowRecord buyOrReadBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElse(null);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        // 1. Проверяем, есть ли уже у юзера доступ к этой книге
        // Чтобы он не покупал одну и ту же книгу дважды
        boolean alreadyHas = borrowRecordRepository.existsByUserAndBook(user, book);
        if (alreadyHas) {
            throw new RuntimeException("Книга уже есть в вашей библиотеке");
        }

        // 2. Логика оплаты (если книга платная)
        // Предполагаем, что в классе Book есть поле price
        if (user.getBalance() < book.getPrice()) {
            throw new RuntimeException("Недостаточно баллов/сомов на балансе!");
        }

        // 3. Списываем баллы
        user.setBalance(user.getBalance() - book.getPrice());
        userRepository.save(user); // Обновляем баланс юзера

        // 4. Создаем запись о доступе
        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setUser(user);
        record.setBorrowDate(LocalDateTime.now());
        record.setLastReadPage(0); // Начинаем с 0 страницы
        record.setPurchased(true);

        return borrowRecordRepository.save(record);
    }

    @Transactional
    public void updateProgress(Long recordId, int newPage) {
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Запись не найдена"));

        // Считаем новые страницы
        int pagesGained = newPage - record.getLastReadPage();

        if (pagesGained > 0) {
            // Начисляем баллы юзеру
            User user = record.getUser();
            user.setBalance(user.getBalance() + pagesGained);
            userRepository.save(user);

            // Обновляем прогресс
            record.setLastReadPage(newPage);
            borrowRecordRepository.save(record);
        }
    }
}