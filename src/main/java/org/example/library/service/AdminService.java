package org.example.library.service;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.request.BookRequest;
import org.example.library.dto.request.UserRegisterRequest;
import org.example.library.dto.request.UserRequest;
import org.example.library.dto.response.BookResponse;
import org.example.library.dto.response.OrderResponse;
import org.example.library.dto.response.UserResponse;
import org.example.library.model.Book;
import org.example.library.model.Order;
import org.example.library.model.OrderStatus;
import org.example.library.model.User;
import org.example.library.repositories.BookRepository;
import org.example.library.repositories.OrderRepository;
import org.example.library.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final BookService bookService;
    private final OrderService orderService;

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsersForAdmin() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse registerUser(UserRegisterRequest request) {
        return userService.registerUser(request);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        return userService.updateUser(id, request);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public UserResponse getProfile(Long id) {
        return userService.getProfile(id);
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Transactional
    public void addBook(BookRequest request) {
        bookService.addBook(request);
    }

    @Transactional
    public void updateBook(Long id, BookRequest request) {
        bookService.updateBook(id, request);
    }

    @Transactional
    public void deleteBook(Long id) {
        bookService.deleteBook(id);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrdersAsDto() {
        return orderService.getAllOrders().stream()
                .map(OrderResponse::fromEntity)
                .toList();
    }

    @Transactional
    public void updateOrderStatus(Long id, OrderStatus status) {
        orderService.updateOrderStatus(id, status);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getAdminDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalBooks", bookRepository.count());
        stats.put("totalOrders", orderRepository.count());

        Double revenue = orderRepository.calculateTotalRevenue();
        stats.put("totalRevenue", revenue != null ? revenue : 0.0);

        return stats;
    }


    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .userEmail(order.getUser().getEmail())
                .build();
    }
}
