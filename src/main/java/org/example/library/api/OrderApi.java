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

    // Купить книгу или взять почитать
    @PostMapping("/buy")
    public ResponseEntity<OrderResponse> buy(@RequestParam Long userId, @RequestParam Long bookId) {
        // Минимум кода: вызвали сервис и вернули результат
        OrderResponse response = orderService.buyBook(userId, bookId);
        return ResponseEntity.ok(response);
    }


    // Обновить прогресс и получить баллы
//    @PatchMapping("/progress")
//    public ResponseEntity<?> updateProgress(@RequestParam Long recordId, @RequestParam int pages) {
//        try {
//            orderService.updateProgress(recordId, pages);
//            return ResponseEntity.ok("Прогресс обновлен, баллы зачислены!");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
}