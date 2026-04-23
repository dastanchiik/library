package org.example.library.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.library.dto.request.UserRegisterRequest;
import org.example.library.dto.request.UserRequest;
import org.example.library.dto.response.BookResponse;
import org.example.library.dto.response.OrderResponse;
import org.example.library.dto.response.UserResponse;
import org.example.library.model.Role;
import org.example.library.model.User;
import org.example.library.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.map(User::getId).orElse(null));
        userResponse.setEmail(user.map(User::getEmail).orElse(null));
        userResponse.setFullName(user.map(User::getFullName).orElse(null));
        userResponse.setBalance(user.map(User::getBalance).orElse(null));
        userResponse.setUsername(user.map(User::getUsername).orElse(null));
        return userResponse;
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public List<User> searchByUsernameOrFullName(String universalName) {
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

    // ===== АДМИН МЕТОДЫ =====

    public User getOrCreateAdmin() {
        Optional<User> existingAdmin = userRepository.findByEmail("admin@bookcloud.com");

        if (existingAdmin.isPresent()) {
            return existingAdmin.get();
        }

        // Создаем админа
        User admin = User.builder()
                .fullName("Администратор BookCloud")
                .email("admin@bookcloud.com")
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .balance(0.0)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(admin);
    }

    public UserResponse registerUser(UserRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .balance(0.0)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setFullName(savedUser.getFullName());
        response.setEmail(savedUser.getEmail());
        response.setBalance(savedUser.getBalance());
        return response;
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        // 1. Ищем пользователя или выбрасываем четкое исключение
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));

        // 2. Проверка на дубликат Email (если email изменился)
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Этот Email уже занят другим пользователем");
        }

        // 3. Обновляем основные поля
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        // Если у тебя в модели есть поле username, обновляем и его
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }

        // 4. Обновляем роль и баланс (те поля, которые мы видели на фронтенде)
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        if (request.getBalance() != null) {
            user.setBalance(request.getBalance());
        }

        // 5. Безопасное обновление пароля
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // 6. Сохраняем
        User savedUser = userRepository.save(user);

        // 7. Превращаем в Response (лучше вынести в отдельный метод-маппер)
        return mapToResponse(savedUser);
    }

    // Вспомогательный метод для чистоты кода
    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole()); // Добавил роль
        response.setBalance(user.getBalance()); // Теперь баланс не будет приходить как undefined
        return response;
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        userRepository.delete(user);
    }

    public List<org.example.library.dto.response.OrderResponse> getAllOrdersAsDto() {
        List<org.example.library.model.Order> allOrders = userRepository.findAll().stream()
                .flatMap(user -> user.getOrders().stream())
                .collect(Collectors.toList());
        
        return allOrders.stream()
                .map(order -> org.example.library.dto.response.OrderResponse.fromEntity(order))
                .collect(Collectors.toList());
    }
}
