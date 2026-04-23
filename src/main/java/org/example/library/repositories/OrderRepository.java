package org.example.library.repositories;

import org.example.library.model.Book;
import org.example.library.model.Order;
import org.example.library.model.OrderStatus;
import org.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
        boolean existsByUserAndBook(User user, Book book);
        List<Order> findByUser(User user);
        List<Order> findByStatus(OrderStatus status);
        @Query("SELECT SUM(o.totalPrice) FROM Order o")
        Double calculateTotalRevenue();

        List<Order> findAllByBook_Isbn(String isbn);
        
        List<Order> findAllByUser_Username(String username);
}
