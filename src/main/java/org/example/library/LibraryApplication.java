package org.example.library;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.library.model.Role;
import org.example.library.model.User;
import org.example.library.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class LibraryApplication {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }
//    @PostConstruct
//    public void firstMethod() {
//        User user = new User();
//        user.setFullName("Admin Dastan");
//        user.setEmail("admin@bookcloud.com");
//        user.setPassword(passwordEncoder.encode("admin123"));
//        user.setRole(Role.ADMIN);
//        userRepository.save(user);
//        System.out.println("This is the first method.");
//    }

}
