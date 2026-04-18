package org.example.library.service;

import lombok.RequiredArgsConstructor;
import org.example.library.model.Book;
import org.example.library.model.Order;
import org.example.library.model.OrderStatus; // Твой Enum
import org.example.library.model.User;
import org.example.library.repositories.BookRepository;
import org.example.library.repositories.OrderRepository;
import org.example.library.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public Order createOrder(Long userId, Long bookId) {
        // Ищем юзера и книгу
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        // 1. Проверяем наличие на складе (stock)
        if (book.getStock() <= 0) {
            throw new RuntimeException("Извините, книги '" + book.getTitle() + "' нет в наличии");
        }

        // 2. Проверяем баланс (сомы/баллы)
        if (user.getBalance() < book.getPrice()) {
            throw new RuntimeException("Брат, не хватает средств на балансе!");
        }

        // 3. Бизнес-логика сделки
        user.setBalance(user.getBalance() - book.getPrice()); // Списываем деньги
        book.setStock(book.getStock() - 1); // Уменьшаем остаток в магазине

        // 4. Создаем заказ
        Order order = Order.builder()
                .user(user)
                .book(book)
                .orderDate(LocalDateTime.now()) // Поменяй borrowDate на orderDate в модели
                .status(OrderStatus.PAID) // Ставим статус ОПЛАЧЕНО
                .totalPrice(book.getPrice()) // Фиксируем цену покупки
                .build();

        // Сохраняем изменения (благодаря @Transactional всё сохранится вместе)
        userRepository.save(user);
        bookRepository.save(book);
        return orderRepository.save(order);
    }

    @Transactional
    public Order deliverOrder(Long orderId) {
        // Метод для админа: когда клиент пришел в магазин и забрал книгу
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));

        order.setStatus(OrderStatus.DELIVERED);
        return orderRepository.save(order);
    }
}