package org.example.library.api;

import lombok.RequiredArgsConstructor;
import org.example.library.model.Order;
import org.example.library.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
public class BorrowApi {

    private final OrderService orderService;

    // Купить книгу или взять почитать
    @PostMapping("/buy")
    public ResponseEntity<?> buyBook(@RequestParam Long userId, @RequestParam Long bookId) {
        try {
            // В реальном проекте мы бы брали User из SecurityContext, 
            // но для ярмарки и тестов передаем userId параметром
            Order record = orderService.buyOrReadBook(userId, bookId);
            return ResponseEntity.ok(record);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Обновить прогресс и получить баллы
    @PatchMapping("/progress")
    public ResponseEntity<?> updateProgress(@RequestParam Long recordId, @RequestParam int pages) {
        try {
            orderService.updateProgress(recordId, pages);
            return ResponseEntity.ok("Прогресс обновлен, баллы зачислены!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}