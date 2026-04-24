package org.example.library.dto.response;

import lombok.*;
import org.example.library.model.Order;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private String bookTitle;
    private Double price;
    private String customerName;
    private String username;
    private LocalDateTime orderDate;
    private String status;
    private String userEmail;
    private Double totalPrice;
    private Long userId;
    private LocalDateTime createdAt;

    public static OrderResponse fromEntity(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setBookTitle(order.getBook().getTitle());
        response.setPrice(order.getBook().getPrice());
        response.setCustomerName(order.getUser().getFullName());
        response.setUsername(order.getUser().getUsername());
        response.setOrderDate(order.getOrderDate());
        response.setStatus(order.getStatus() != null ? order.getStatus().name() : "UNKNOWN");
        response.setTotalPrice(order.getTotalPrice());
        response.setUserId(order.getUser().getId());
        response.setUserEmail(order.getUser().getEmail());
        response.setCreatedAt(order.getOrderDate());
        return response;
    }
}
