package org.example.library.service;

import lombok.RequiredArgsConstructor;
import org.example.library.dto.response.UserResponse;
import org.example.library.model.User;
import org.example.library.repositories.BookRepository;
import org.example.library.repositories.OrderRepository;
import org.example.library.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsersForAdmin() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getAdminDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalBooks", bookRepository.count());
        stats.put("totalOrders", orderRepository.count());

        Double revenue = orderRepository.calculateTotalRevenue();
        stats.put("totalRevenue", revenue != null ? revenue : 0.0);

        return stats;
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }
}
