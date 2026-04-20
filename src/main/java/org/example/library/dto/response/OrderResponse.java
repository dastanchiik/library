package org.example.library.dto.response;

import lombok.Data;
import org.example.library.model.Order;

import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private Long orderId;
    private String bookTitle;
    private Double price;
    private String customerName; // Имя покупателя вместо всего объекта User
    private LocalDateTime orderDate;

    public static OrderResponse fromEntity(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setBookTitle(order.getBook().getTitle());
        response.setPrice(order.getBook().getPrice());
        response.setCustomerName(order.getUser().getFullName());
        response.setOrderDate(order.getOrderDate());
        return response;
    }
}
