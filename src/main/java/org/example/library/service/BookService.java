package org.example.library.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.library.dto.request.BookRequest;
import org.example.library.model.Book;
import org.example.library.model.Order;
import org.example.library.model.OrderStatus;
import org.example.library.model.User;
import org.example.library.repositories.BookRepository;
import org.example.library.repositories.OrderRepository;
import org.example.library.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public String buyBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        // 1. Проверка на наличие (Stock)
        if (book.getStock() <= 0) {
            throw new RuntimeException("Брат, книги закончились на складе!");
        }

        // 2. Проверка баланса
        if (user.getBalance() < book.getPrice()) {
            throw new RuntimeException("Брат, не хватает баллов!");
        }

        // 3. Логика сделки
        user.setBalance(user.getBalance() - book.getPrice());
        book.setStock(book.getStock() - 1); // Уменьшаем остаток

        Order order = Order.builder()
                .user(user)
                .book(book)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PAID) // Сразу ставим статус PAID (Оплачено)
                .totalPrice(book.getPrice())
                .build();

        orderRepository.save(order);
        bookRepository.save(book);
        userRepository.save(user);

        return "ORDER_CREATED_AND_PAID";
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
        book.setStock(bookRequest.getStock()); // Не забудь добавить в BookRequest поле stock
        book.setIsbn(isbn);

        return bookRepository.save(book);
    }

    public String updateBook(Long id, BookRequest bookRequest) {
        Book bookToUpdate = getBookById(id);
        bookToUpdate.setTitle(bookRequest.getTitle());
        bookToUpdate.setAuthor(bookRequest.getAuthor());
        bookToUpdate.setPrice(bookRequest.getPrice()); // Обновляем и цену
        bookToUpdate.setStock(bookRequest.getStock()); // И наличие
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