package org.example.library.service;

import org.example.library.dto.response.UserResponse;
import org.example.library.model.User;
import org.example.library.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
    public List<User>searchByUsernameOrFullName(String universalName) {
        return userRepository.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(universalName, universalName);
    }
    public UserResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        UserResponse resp = new UserResponse();
        resp.setId(user.getId());
        resp.setFullName(user.getFullName());
        resp.setEmail(user.getEmail());
        resp.setBalance(user.getBalance());

        // 1. Считаем книги
        int booksCount = user.getOrders().size();
        resp.setTotalBooks(booksCount);

        // 2. Считаем дни (ChronoUnit помогает вычислить разницу)
        long days = ChronoUnit.DAYS.between(user.getCreatedAt(), LocalDateTime.now());
        resp.setDaysWithUs(days);

        // 3. Рассчитываем ранг (простая логика)
        if (booksCount == 0) resp.setRank("Странник");
        else if (booksCount < 3) resp.setRank("Читатель");
        else if (booksCount < 7) resp.setRank("Книжный червь");
        else resp.setRank("Легендарный Библиотекарь");

        // 4. Ищем любимый жанр (Stream API магия)
        String fav = user.getOrders().stream()
                .map(order -> order.getBook().getCategory().name()) // Берем имена категорий
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) // Группируем и считаем
                .entrySet().stream()
                .max(Map.Entry.comparingByValue()) // Ищем тот, которого больше всего
                .map(Map.Entry::getKey)
                .orElse("Ещё не определен");

        resp.setFavouriteGenre(fav);

        return resp;
    }

}
