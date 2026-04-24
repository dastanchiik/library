package org.example.library.service;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.response.OrderResponse;
import org.example.library.model.Book;
import org.example.library.model.Order;
import org.example.library.model.OrderStatus;
import org.example.library.model.User;
import org.example.library.repositories.BookRepository;
import org.example.library.repositories.OrderRepository;
import org.example.library.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;


        @Transactional
        public OrderResponse buyBook(Long userId, Long bookId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Книга не найдена"));

            if (user.getBalance() < book.getPrice()) {
                throw new RuntimeException("Недостаточно средств");
            }
            if (book.getStock() <= 0) {
                throw new RuntimeException("Книги нет в наличии");
            }

            book.setSalesCount(book.getSalesCount() + 1);
            bookRepository.save(book);
            user.setBalance(user.getBalance() - book.getPrice());
            book.setStock(book.getStock() - 1);

            Order order = new Order();
            order.setUser(user);
            order.setBook(book);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(OrderStatus.PENDING);
            order.setTotalPrice(book.getPrice());
            orderRepository.save(order);

            return OrderResponse.fromEntity(order);
        }



    public List<Order> getUserOrders(Long userId) {
        return null;
    }

    @Transactional
    public Order deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));

        order.setStatus(OrderStatus.DELIVERED);
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<OrderResponse> getByIsbn(String isbn) {
            return orderRepository.findAllByBook_Isbn(isbn)
                    .stream()
                    .map(OrderResponse::fromEntity)
                    .toList();
    }

    public List<OrderResponse> getByUsername(String username) {
            return orderRepository.findAllByUser_Username(username)
                    .stream()
                    .map(OrderResponse::fromEntity)
                    .toList();
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));
        return OrderResponse.fromEntity(order);
    }

    @Transactional
    public void updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));
        orderRepository.delete(order);
    }

    public java.util.Map<String, Object> getAdminDashboardStats() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();

        stats.put("totalOrders", orderRepository.count());

        Double totalRevenue = 0.0;
        List<Order> allOrders = orderRepository.findAll();
        for (Order order : allOrders) {
            if (order.getTotalPrice() != null) {
                totalRevenue += order.getTotalPrice();
            }
        }
        stats.put("totalRevenue", totalRevenue);

        long completedOrders = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.COMPLETED)
                .count();
        long pendingOrders = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.PENDING)
                .count();

        stats.put("completedOrders", completedOrders);
        stats.put("pendingOrders", pendingOrders);

        return stats;
    }
}
