package org.example.library.api;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.response.OrderResponse;
import org.example.library.model.Order;
import org.example.library.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderApi {

    private final OrderService orderService;

    @PostMapping("/buy")
    public ResponseEntity<OrderResponse> buy(@RequestParam Long userId, @RequestParam Long bookId) {
        OrderResponse response = orderService.buyBook(userId, bookId);
        return ResponseEntity.ok(response);
    }
}