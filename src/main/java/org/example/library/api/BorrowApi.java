package org.example.library.api;

import lombok.RequiredArgsConstructor;
import org.example.library.model.BorrowRecord;
import org.example.library.service.BorrowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
public class BorrowApi {

    private final BorrowService borrowService;

    // Купить книгу или взять почитать
    @PostMapping("/buy")
    public ResponseEntity<?> buyBook(@RequestParam Long userId, @RequestParam Long bookId) {
        try {
            // В реальном проекте мы бы брали User из SecurityContext, 
            // но для ярмарки и тестов передаем userId параметром
            BorrowRecord record = borrowService.buyOrReadBook(userId, bookId);
            return ResponseEntity.ok(record);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Обновить прогресс и получить баллы
    @PatchMapping("/progress")
    public ResponseEntity<?> updateProgress(@RequestParam Long recordId, @RequestParam int pages) {
        try {
            borrowService.updateProgress(recordId, pages);
            return ResponseEntity.ok("Прогресс обновлен, баллы зачислены!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}