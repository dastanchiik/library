package org.example.library.api;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.request.BookRequest;
import org.example.library.dto.request.UserRegisterRequest;
import org.example.library.dto.request.UserRequest;
import org.example.library.dto.response.BookResponse;
import org.example.library.dto.response.OrderResponse;
import org.example.library.dto.response.UserResponse;
import org.example.library.model.Category;
import org.example.library.model.OrderStatus;
import org.example.library.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminApi {

    private final AdminService adminService;
    private final OrderService orderService;
    private final BookService bookService;
    private final ImageUploadService imageUploadService;

    // ===== ПОЛЬЗОВАТЕЛИ =====
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsersForAdmin());
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok(adminService.registerUser(request));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getProfile(id));
    }

    @PutMapping("/users/update/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(adminService.updateUser(id, request));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "Пользователь удален успешно"));
    }

    // ===== КАТЕГОРИИ =====
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(
                Arrays.stream(Category.values())
                        .map(Enum::name)
                        .toList()
        );
    }

    // ===== КНИГИ =====
    @GetMapping("/search/book/title")
    public ResponseEntity<List<BookResponse>> getAllBooksByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.searchBooks(title));
    }

    @GetMapping("/search/book/category")
    public ResponseEntity<List<BookResponse>> getAllBooksByCategory(@RequestParam String category) {
        return ResponseEntity.ok(bookService.getBooksByCategory(category));
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.ok(adminService.getAllBooks());
    }

    @PostMapping("/books")
    public ResponseEntity<Map<String, String>> createBook(@RequestBody BookRequest request) {
        adminService.addBook(request);
        return ResponseEntity.ok(Map.of("message", "Книга добавлена"));
    }

    @PostMapping("/books/upload-image")
    public ResponseEntity<Map<String, String>> uploadBookImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageURL = imageUploadService.uploadImage(file);
            return ResponseEntity.ok(Map.of("imageURL", imageURL));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Ошибка при загрузке изображения: " + e.getMessage()));
        }
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Map<String, String>> updateBook(@PathVariable Long id, @RequestBody BookRequest request) {
        adminService.updateBook(id, request);
        return ResponseEntity.ok(Map.of("message", "Книга обновлена"));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Map<String, String>> deleteBook(@PathVariable Long id) {
        adminService.deleteBook(id);
        return ResponseEntity.ok(Map.of("message", "Книга удалена"));
    }

    // ===== ЗАКАЗЫ =====
    @GetMapping("/orders/search/isbn")
    public ResponseEntity<List<OrderResponse>> searchOrders(@RequestParam String isbn) {
        return ResponseEntity.ok(orderService.getByIsbn(isbn));
    }

    @GetMapping("/orders/search/username")
    public ResponseEntity<List<OrderResponse>> searchOrdersByUsername(@RequestParam String username) {
        return ResponseEntity.ok(orderService.getByUsername(username));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(adminService.getAllOrdersAsDto());
    }

    @GetMapping("/get/one/order/{id}")
    public ResponseEntity<OrderResponse> getOneOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<Map<String, String>> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusRequest) {

        String statusStr = statusRequest.get("status");
        if (statusStr == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Поле 'status' обязательно"));
        }

        try {
            OrderStatus status = OrderStatus.valueOf(statusStr.toUpperCase());
            adminService.updateOrderStatus(id, status);
            return ResponseEntity.ok(Map.of("message", "Статус заказа #" + id + " изменен на " + status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Неверный статус: " + statusStr));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(adminService.getAdminDashboardStats());
    }
}
