package org.example.library.dto.response;

import lombok.*;
import org.example.library.model.Order;

import java.time.LocalDateTime;

//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private String bookTitle;
    private Double price;
    private String customerName; // Имя покупателя вместо всего объекта User
    private LocalDateTime orderDate;
    private String status;
    private String userEmail; // Добавляем email для удобства
    private Double totalPrice;
    private Long userId;
    private LocalDateTime createdAt; // Новое поле для совместимости с JavaScript кодом

    public static OrderResponse fromEntity(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setBookTitle(order.getBook().getTitle());
        response.setPrice(order.getBook().getPrice());
        response.setCustomerName(order.getUser().getFullName());
        response.setOrderDate(order.getOrderDate());
        response.setStatus(order.getStatus() != null ? order.getStatus().name() : "UNKNOWN");
        response.setTotalPrice(order.getTotalPrice());
        response.setUserId(order.getUser().getId());
        response.setCreatedAt(order.getOrderDate()); // Используем orderDate как createdAt
        return response;
    }
}
