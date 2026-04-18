package org.example.library.repositories;

import org.example.library.model.Book;
import org.example.library.model.Order;
import org.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
        boolean existsByUserAndBook(User user, Book book);
        List<Order> findByUser(User user);
        List<Order> findByStatus(String status);
}
