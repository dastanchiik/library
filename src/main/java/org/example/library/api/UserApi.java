package org.example.library.api;

import lombok.RequiredArgsConstructor;
import org.example.library.model.User;
import org.example.library.repositories.UserRepository;
import org.example.library.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/user")
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;
    private final UserRepository userRepository;

    @PatchMapping("/{userId}/add-money")
    public ResponseEntity<?> addMoney(@PathVariable Long userId, @RequestParam int amount) {
        User user = userService.getById(userId).get();
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        return ResponseEntity.ok("Баланс пополнен на " + amount);
    }
}
