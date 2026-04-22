package org.example.library.api;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.response.UserResponse;
import org.example.library.model.User;
import org.example.library.repositories.UserRepository;
import org.example.library.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;
    private final UserRepository userRepository;

    @PatchMapping("/add-money")
    public ResponseEntity<?> addMoney(@RequestParam Long userId, @RequestParam int amount) {
        User user = userService.getById(userId).get();
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        return ResponseEntity.ok("Баланс пополнен на " + amount);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/by-email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        // В сервисе должен быть метод findByEmail
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/search/user")
    public List<User> searchUser(@RequestParam String query) {
        return userService.searchByUsernameOrFullName(query);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile(@RequestParam Long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

}
